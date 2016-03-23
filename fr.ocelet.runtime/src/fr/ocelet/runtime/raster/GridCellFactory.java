package fr.ocelet.runtime.raster;

public class GridCellFactory {

	
	public static GridCellManager create(String cellType, Grid grid){
		
		if(cellType.equals("HEXAGONAL")){
				return new GridHexaCellManager(grid);
		}
		if(cellType.equals("QUADRILATERAL")){
			return new GridQuadriCellManager(grid);
		}
		if(cellType.equals("TRIANGULAR")){
			return new GridTriangularCellManager(grid);
		}
		return null;
		
		}
	
	
public static GridCellManager create(Grid grid){
		String cellType = grid.getCellShapeType();
		if(cellType.equals("HEXAGONAL")){
				return new GridHexaCellManager(grid);
		}
		if(cellType.equals("QUADRILATERAL")){
			return new GridQuadriCellManager(grid);
		}
		if(cellType.equals("TRIANGULAR")){
			return new GridTriangularCellManager(grid);
		}
		return null;
		
		}
}
