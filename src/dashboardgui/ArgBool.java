package dashboardgui;

public class ArgBool extends Argument{
	
	private String style = "bool";
	
        @Override
	public String getStyle() {
		return style;
	}
        @Override
	public void setStyle(String style) {
		this.style = style;
	}

}
