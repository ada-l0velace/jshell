package pt.tecnico.myDrive.service.dto;

/**
 * Created by lolstorm on 26/04/16.
 */
public class EnvironmentVariableDto {

	private String _name;
	private String _value;
	
	public EnvironmentVariableDto(String name, String value){
		
		_name = name;
		_value = value;
			
	}
	public String getName(){
		return _name;
	}
	
	public String getValue(){
		return  _value;

	}
	
}
