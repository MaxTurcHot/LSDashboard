package batchtool;

public abstract class Argument {

	private String data;
	private String style;
	private String datalabel;
	
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public String getDatalabel() {
		return datalabel;
	}
	public void setDatalabel(String datalabel) {
		this.datalabel = datalabel;
	}
	

}
