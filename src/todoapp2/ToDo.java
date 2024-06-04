package todoapp2;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//実行用設計図
class ToDo{
	//フィールド:オブジェクト指向プログラミングにおいて、クラスやオブジェクトでカプセル化されたデータ
	private int id;
  private String title;
  private int importance;
  private String status;
  private String createdAt;
  private String updatedAt;

	//コンストラクタ：クラスをnewしたとき（インスタンスを作ったとき）に実行される関数
  public ToDo(int id, String title, int importance, String status, String createdAt, String updatedAt) {
      this.id = id;
      this.title = title;
      this.importance = importance;
      this.status = status;
      this.createdAt = createdAt;
      this.updatedAt = updatedAt;
  }
	
	//インスタンスメソッド:コンストラクタが定義されたクラス内において、インスタンス自身を紐づかせるためのメソッド
  public String showStatus() {
      return String.format("ID:%d タイトル:%s 重要度:%d ステータス:%s 作成日:%s 更新日:%s", 
          this.id, this.title, this.importance, this.status, this.createdAt, this.updatedAt);
  }

	public void changeImportance(int importance) {
		this.importance=importance;
		System.out.println("重要度を変更しました。");
	}
	public void changeTitle(String title) {
      this.title = title;
      System.out.println("ToDo内容を変更しました。");
  }

	public void changeStatus(String status) {
      this.status = status;
      System.out.println("ステータスを変更しました。");
  }

	public void updateTimestamp() {
      this.updatedAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
  }
  
	public int getId() {
      return this.id;
  }


	public int getImportance() {
      return this.importance;
  }

	
	//インスタンスの情報をcsvにするメソッド
	public String toCSV() {
      return String.format("%d,%s,%d,%s,%s,%s", this.id, this.title, this.importance, this.status, this.createdAt, this.updatedAt);
  }
}