package dashboardgui;

public abstract class Argument {

	private String data;
	private String style;
	private String datalabel;
	
	public String getData() {
		return data;
	}
        public String getAbsData(String basepath) {
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
	
        public void selector() {
            //return null;
        }
        
        public void selector(String basepath) {
            //return null;
        }
        
        public void selector(String basepath, Boolean donotexist) {
            //return null;
        }
        
        public boolean valid(String basepath) {
            return false;
        }

}
