package fr.ocelet.lang.ui;

import static com.google.common.collect.Maps.newHashMap;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import com.google.common.cache.CacheStats;


public class PlatformURIMapCache {

    private final static Logger LOG = Logger.getLogger(PlatformURIMapCache.class);
    
    private Map<CacheKey, Map<URI, URI>> cache = newHashMap();
    private long hitCount;
    private long missCount;
    private long evictCount;
    
    public PlatformURIMapCache() {
        LOG.debug("PlatformURIMapCache new instance");
    }
    
    public Map<URI, URI> computePlatformURIMap(IJavaProject javaProject) {
        HashMap<URI, URI> hashMap = newHashMap(computePlatformURIMap());
        
        try {
            if (!javaProject.exists())
                return hashMap;
            
            IClasspathEntry[] classpath = javaProject.getResolvedClasspath(true);
            for (IClasspathEntry classPathEntry : classpath) {
                processClasspathEntry( hashMap, classPathEntry );
            }
        } catch (JavaModelException e) {
            LOG.error(e.getMessage(), e);
        }
        return hashMap;
    }

    private boolean slow = !Boolean.getBoolean( "cacheComputePlatformURIMap" );
    
    private Map<URI, URI> computePlatformURIMap() {
        
        if( slow ) {
            return EcorePlugin.computePlatformURIMap();
        }
        
        HashMap<URI, URI> result = newHashMap();
        
        result.putAll(computePlatformPluginToPlatformResourceMap());
        result.putAll(EcorePlugin.computePlatformResourceToPlatformPluginMap(new HashSet<URI>(EcorePlugin.getEPackageNsURIToGenModelLocationMap().values())));
        
        return EcorePlugin.computePlatformURIMap();
    }
    
    private static Pattern bundleSymbolNamePattern = Pattern.compile( "^\\s*Bundle-SymbolicName\\s*:\\s*([^\\s;]*)\\s*(;.*)?$", Pattern.MULTILINE );
    
    private Map<? extends URI, ? extends URI> computePlatformPluginToPlatformResourceMap() {
        IWorkspaceRoot root = EcorePlugin.getWorkspaceRoot();
        if( null == root ) {
            return Collections.emptyMap();
        }

        IProject[] projects = root.getProjects();
        if( null == projects ) {
            return Collections.emptyMap();
        }
        
        Map<URI, URI> result = new HashMap<URI, URI>();
        
        Handler handler = new Handler();

        for( int i = 0, size = projects.length; i < size; ++i )
        {
            IProject project = projects[ i ];
            
            result.putAll( handler.processProject( project ) );
        }

        return result;
    }
    
    class Handler extends DefaultHandler
    {
        public String pluginID;

        private boolean createParser = true;
        private SAXParser parser = null;
        
        public SAXParser getParser() {
            
            if( createParser ) {
                createParser = false;
                
                SAXParserFactory parserFactory = SAXParserFactory.newInstance();
                parserFactory.setNamespaceAware( true );

                try
                {
                    parser = parserFactory.newSAXParser();
                } catch( Exception exception )
                {
                    LOG.error( exception );
                }
            }
            
            return parser;
        }
        
        private Map<URI, URI> result;
        private IFile manifest;
        private IFile plugin;
        private CacheKey key;

        public Map<URI, URI> processProject( IProject project ) {
            manifest = project.getFile( "META-INF/MANIFEST.MF" );
            plugin = project.getFile( "plugin.xml" );
            
            result = null;
            
            getPluginID( project );
            
            if( null == result ) {
                result = Collections.emptyMap();
            }
            return result;
        }
        
        public String getPluginID( IProject project ) {
            if( ! project.isOpen() ) {
                return null;
            }
            
            pluginID = null;
            
            if( manifest.exists() ) {
                readManifest();
            } else if( plugin.exists() ) {
                readPluginXml( plugin );
            }

            if( result == null )
            {
                if( null == pluginID ) {
                    return null;
                }
                
                result = newHashMap();

                URI platformPluginURI = URI.createPlatformPluginURI( pluginID + "/", false );
                URI platformResourceURI = URI.createPlatformResourceURI( project.getName() + "/", true );
                result.put( platformPluginURI, platformResourceURI );
                
                cache.put( key, result );
                missCount ++;
            } else {
                hitCount ++;
            }
            
            return pluginID;
        }
        
        private void readPluginXml( final IFile plugin ) {
            
            if( null == getParser() ) {
                return;
            }
            
            key = new CacheKey( plugin );
            result = cache.get( key );
            if( null != result ) {
                return;
            }
            
            try
            {
                getParser().parse( new InputSource( plugin.getContents() ), this );
            } catch( Exception exception )
            {
                if( pluginID == null )
                {
                    LOG.error( exception );
                }
            }
        }

        private void readManifest() {
            
            key = new CacheKey( manifest );
            result = cache.get( key );
            if( null != result ) {
                hitCount ++;
                return;
            }
            
            missCount ++;
            InputStream inputStream = null;
            try
            {
                inputStream = manifest.getContents();
                int available = inputStream.available();
                if( bytes.length < available )
                {
                    bytes = new byte[available];
                }
                inputStream.read( bytes );
                String contents = new String( bytes, "UTF-8" );
                Matcher matcher = bundleSymbolNamePattern.matcher( contents );
                if( matcher.find() )
                {
                    pluginID = matcher.group( 1 );
                }
            } catch( Exception exception )
            {
                LOG.error( exception );
            } finally
            {
                if( inputStream != null )
                {
                    try
                    {
                        inputStream.close();
                    } catch( IOException exception )
                    {
                        LOG.error( exception );
                    }
                }
            }
        }
        
        byte[] bytes = {};

        @Override
        public void startElement( String uri, String localName, String qName, Attributes attributes ) throws SAXException
        {
            if( "".equals( uri ) && "plugin".equals( localName ) )
            {
                pluginID = attributes.getValue( "id" );
            }
            throw new SAXException( "Done" );
        }
    }

    public CacheStats getStats() {
        return new CacheStats( hitCount, missCount, cache.size(), 0, 0, evictCount );
    }

    protected void processClasspathEntry( HashMap<URI, URI> hashMap, IClasspathEntry classPathEntry ) {
        
        IPath path = classPathEntry.getPath();
        if (null == path || ! "jar".equals(path.getFileExtension())) {
            return;
        }
        
        try {
            final File file = path.toFile();
            
            if (null == file || ! file.exists()) {
                return;
            }
            
            processJarFile(hashMap, file);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    protected void processJarFile( HashMap<URI, URI> hashMap, final File file ) throws IOException {
        CacheKey key = new CacheKey( file );
        
        Map<URI, URI> cached = cache.get(key);
        if (null == cached) {
            cached = mapFromJarFile(file);
            
            removeStaleEntry(file);
            
            cache.put(key, cached);
            missCount ++;
        } else {
            hitCount ++;
        }
        
        hashMap.putAll(cached);
    }

    private void removeStaleEntry(File file) {
        String path = file.getAbsolutePath();
        
        for(Iterator<CacheKey> iter = cache.keySet().iterator(); iter.hasNext(); ) {
            CacheKey key = iter.next();
            if(key.matches(path)) {
                iter.remove();
                evictCount ++;
            }
        }
    }

    private Map<URI, URI> mapFromJarFile( File file ) throws IOException {
        JarFile jarFile = new JarFile(file);
        try {
            Manifest manifest = jarFile.getManifest();
            
            if (null == manifest) {
                return Collections.emptyMap();
            }
            
            String name = manifest.getMainAttributes().getValue("Bundle-SymbolicName");
            if (null == name) {
                return Collections.emptyMap();
            }
            
            name = stripSemicolon( name );
            if (EcorePlugin.getPlatformResourceMap().containsKey(name)) {
                return Collections.emptyMap();
            }
            
            String p = "archive:" + file.toURI() + "!/";
            URI uri = URI.createURI(p);
            final URI platformResourceKey = URI.createPlatformResourceURI(name + "/", false);
            final URI platformPluginKey = URI.createPlatformPluginURI(name + "/", false);

            
            Map<URI, URI> result = newHashMap();
            result.put(platformResourceKey, uri);
            result.put(platformPluginKey, uri);
        
            return result;
        } finally {
            jarFile.close();
        }
    }

    protected String stripSemicolon( String name ) {
        final int indexOf = name.indexOf(';');
        if (indexOf > 0)
            name = name.substring(0, indexOf);
        return name;
    }

    private static class CacheKey {
        
        private String path;
        private long size;
        private long lastModified;
        
        public CacheKey( File file ) {
            path = file.getAbsolutePath();
            size = file.length();
            lastModified = file.lastModified();
        }

        public CacheKey( IFile file ) {
            path = file.getLocationURI().toString();
            size = 0; // doesn't exist in the API :-/
            lastModified = file.getModificationStamp();
        }
        
        public boolean matches( String path ) {
            return this.path.equals(path);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + (int) ( lastModified ^ ( lastModified >>> 32 ) );
            result = prime * result + (int) ( size ^ ( size >>> 32 ) );
            result = prime * result + ( ( path == null ) ? 0 : path.hashCode() );
            return result;
        }

        @Override
        public boolean equals( Object obj ) {
            if( this == obj ) {
                return true;
            }
            if( obj == null ) {
                return false;
            }
            if( getClass() != obj.getClass() ) {
                return false;
            }
            CacheKey other = (CacheKey) obj;
            if( lastModified != other.lastModified ) {
                return false;
            }
            if( size != other.size ) {
                return false;
            }
            if( path == null ) {
                if( other.path != null ) {
                    return false;
                }
            } else if( !path.equals( other.path ) ) {
                return false;
            }
            return true;
        }
        
        
    }
}
