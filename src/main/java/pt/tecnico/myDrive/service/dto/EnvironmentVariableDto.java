package pt.tecnico.myDrive.service.dto;

/**
 * Created by lolstorm on 26/04/16.
 */
public class EnvironmentVariableDto {

	private String _name;
	private String _value;

	/**
     * Construtor to EnvironmentVariableDto
     * @param name (String) receives the name of the EnvironmentVariable.
     * @param value (String) receives the value of the EnvironmentVariable.
     */
	
	public EnvironmentVariableDto(String name, String value){
		
		_name = name;
		_value = value;
			
	}

	/**
     * Getter for _name.
     * @return String returns the name of the EnvironmentVariable.
     */

	public String getName(){
		return _name;
	}
	
	/**
     * Getter for _value.
     * @return String returns the value of the EnvironmentVariable.
     */

	public String getValue(){
		return  _value;

	}
	
}
