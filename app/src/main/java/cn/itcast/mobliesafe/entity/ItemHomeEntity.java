package cn.itcast.mobliesafe.entity;

public class ItemHomeEntity {
	private int id;  //项id
	private int imgId; //项图片id
	private String title;  //项标题
	public ItemHomeEntity(int id, int imgId, String title) {
		super();
		this.id = id;
		this.imgId = imgId;
		this.title = title;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getImgId() {
		return imgId;
	}
	public void setImgId(int imgId) {
		this.imgId = imgId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	};
}
