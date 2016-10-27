package domain;

import java.util.Date;

public class News {

	private int id;
	private int academyId;
	private String time;
	private String title;
	private String content;
	private String address;
	private String pic;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAcademyId() {
		return academyId;
	}
	public void setAcademyId(int academyId) {
		this.academyId = academyId;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@Override
	public String toString() {
		return "News [id=" + id + ", academyId=" + academyId + ", time=" + time
				+ ", title=" + title + ", content=" + content + ", address="
				+ address + ", pic=" + pic + "]";
	}
	
	
}
