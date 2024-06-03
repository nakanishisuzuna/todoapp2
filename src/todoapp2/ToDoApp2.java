package todoapp2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

//実装用設計図
public class ToDoApp2 {
	public static void main(String[] args) throws Exception{
		Scanner sc= new Scanner(System.in);
		File file=new File("todo.csv");		
		ArrayList<ToDo>list;
		if(file.exists()) {
			list=loadFile(file);
		}else{
			list=new ArrayList<>();
		}
		if(list.size()==0) {
			System.out.println("ToDoは1件もありません");
		}else {
			displayList(list);
		}
		while(true) {
			System.out.println("ー操作を入力してください。ー");
			System.out.print("1/登録 2/重要度変更 3/編集 4/削除 5/終了>");
			int select=sc.nextInt();
			switch(select) {
			case 1:
				addItem(list,sc);
				break;
			case 2:
				updateItem(list,sc);
				break;
			case 3:
				editItem(list,sc);
				break;
			case 4:
				deleteItem(list,sc);
				break;
			default:
				System.out.println("アプリケーションを終了します。");
				return;
			}
			displayList(list);
		}
	}
	static void sortList(ArrayList<ToDo> list) {
	    for (int i = 0; i < list.size() - 1; i++) {
	        for (int j = 0; j < list.size() - 1 - i; j++) {
	            if (list.get(j).getImportance() < list.get(j + 1).getImportance()) {
	                ToDo temp = list.get(j);
	                list.set(j, list.get(j + 1));
	                list.set(j + 1, temp);
	            }
	        }
	    }
	}

	
	static void displayList(ArrayList<ToDo>list) {
		sortList(list);
		for(int i=0;i<list.size();i++) {
			System.out.printf("%d・・・%s%n",i,list.get(i).showStatus());
		}
	}
	
	static void addItem(ArrayList<ToDo> list, Scanner sc) {
        System.out.println("新規ToDoを作成します。");
        System.out.print("Todo内容を入力してください>>");
        sc.nextLine(); // nextInt によって残された改行文字を消費します
        String title = sc.nextLine();
        System.out.print("重要度を1～10(最大)で入力してください>>");
        int importance = sc.nextInt();
        sc.nextLine(); // nextInt によって残された改行文字を消費します
        System.out.print("ステータスを入力してください（未着手、進行中、完了）>>");
        String status = sc.nextLine();
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        ToDo t = new ToDo(list.size() + 1, title, importance, status, now, now);
        list.add(t);
    }

	static void updateItem(ArrayList<ToDo> list,Scanner sc) {
		if(list.size()==0) {
			System.out.println("まだToDoがありません");
			return;
		}
		System.out.print("重要度を変更します。IDを入力してください>>");
        int id = sc.nextInt();
        ToDo t = findItemById(list, id);
        if (t == null) {
            System.out.println("指定されたIDのToDoが見つかりません。");
            return;
        }
        System.out.print("重要度を再設定してください>>");
        int importance = sc.nextInt();
        t.changeImportance(importance);
        t.updateTimestamp();

	}
	
	static void editItem(ArrayList<ToDo> list, Scanner sc) {
        if (list.size() == 0) {
            System.out.println("まだToDoがありません");
            return;
        }
        System.out.print("ToDoを編集します。IDを入力してください>>");
        int id = sc.nextInt();
        sc.nextLine(); // Consume the newline character left by nextInt
        ToDo t = findItemById(list, id);
        if (t == null) {
            System.out.println("指定されたIDのToDoが見つかりません。");
            return;
            }

        System.out.print("新しいTodo内容を入力してください>>");
        String title = sc.nextLine();
        System.out.print("新しいステータスを入力してください（未着手、進行中、完了）>>");
        String status = sc.nextLine();
        t.changeTitle(title);
        t.changeStatus(status);
        t.updateTimestamp();
    }

	static void deleteItem(ArrayList<ToDo> list,Scanner sc) {
		System.out.print("Todoを削除します。IDを入力してください>>");
        int id = sc.nextInt();
        ToDo t = findItemById(list, id);
        if (t == null) {
            System.out.println("指定されたIDのToDoが見つかりません。");
            return;
        }
		list.remove(t);
		System.out.println("1件削除しました。");
	}
	
	static ArrayList<ToDo> loadFile(File file) throws Exception{
		//リターンするlistを作成
		ArrayList<ToDo> list=new ArrayList<>();
		//読み込みはfis
		FileInputStream fis=new FileInputStream(file);
		InputStreamReader isr=new InputStreamReader(fis,"UTF-8");
		BufferedReader br=new BufferedReader(isr);
		//読み込み時のイディオム
		String line;
		while((line=br.readLine())!=null) {
			//csvデータなのでカンマでsplit
			String[] values=line.split(",");
			int id = Integer.parseInt(values[0]);
			//タイトルの取り出し
			String title = values[1];
			//重要度の取り出し
			int importance=Integer.parseInt(values[2]);
			String status = values[3];
            String createdAt = values[4];
            String updatedAt = values[5];
			//2つの情報をもとにToDoインスタンスを作成
            ToDo t = new ToDo(id, title, importance, status, createdAt, updatedAt);
			//リストに加える
			list.add(t);
		}
		//br終了処理
		br.close();
		//作成されたlistを返却する
		return list;
	}
	static void saveFile(File file,ArrayList<ToDo> list) throws Exception{
		//書き込みはfos
		FileOutputStream fos=new FileOutputStream(file);
		OutputStreamWriter osw=new OutputStreamWriter(fos,"UTF-8");
		BufferedWriter bw=new BufferedWriter(osw);
		
		//引数で受け取ったlistを拡張for文で回す
		for(ToDo c:list) {
			//インスタンスの情報をcsvで書き込む
			bw.write(c.toCSV());;
			//改行
			bw.newLine();
		}
		//ファイルを閉じる
		bw.close();
	}
	static int generateId(ArrayList<ToDo> list) {
        int maxId = 0;
        for (ToDo t : list) {
            if (t.getId() > maxId) {
                maxId = t.getId();
            }
        }
        return maxId + 1;
    }

    static ToDo findItemById(ArrayList<ToDo> list, int id) {
        for (ToDo t : list) {
            if (t.getId() == id) {
                return t;
            }
        }
        return null;
    }

}

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
    ToDo(int id, String title, int importance, String status, String createdAt, String updatedAt) {
        this.id = id;
        this.title = title;
        this.importance = importance;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
	
	//インスタンスメソッド:コンストラクタが定義されたクラス内において、インスタンス自身を紐づかせるためのメソッド
    String showStatus() {
        return String.format("ID:%d タイトル:%s 重要度:%d ステータス:%s 作成日:%s 更新日:%s", 
            this.id, this.title, this.importance, this.status, this.createdAt, this.updatedAt);
    }

	void changeImportance(int importance) {
		this.importance=importance;
		System.out.println("重要度を変更しました。");
	}
	void changeTitle(String title) {
        this.title = title;
        System.out.println("ToDo内容を変更しました。");
    }

    void changeStatus(String status) {
        this.status = status;
        System.out.println("ステータスを変更しました。");
    }

    void updateTimestamp() {
        this.updatedAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    
    int getId() {
        return this.id;
    }


    int getImportance() {
        return this.importance;
    }

	
	//インスタンスの情報をcsvにするメソッド
    String toCSV() {
        return String.format("%d,%s,%d,%s,%s,%s", this.id, this.title, this.importance, this.status, this.createdAt, this.updatedAt);
    }
}