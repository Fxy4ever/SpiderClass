import javax.sound.sampled.Line;
import java.util.ArrayList;
import java.util.List;

public class main {
    public static void main(String[] args) {

        String URL1 = "http://jwzx.cqupt.edu.cn/jwzxtmp/pubBjsearch.php?action=";
        String uid1 = "bjStu";
        String allInfo1 = Analyze.getNetResults(URL1,uid1);
        List<String> Infolist1 = Analyze.ToRegexHtml(allInfo1,"<tr>","</tr>",2);
        Infolist1.remove(0);
        List<String> a ;
        List<String> b ;
        List<String> classUid1 ;
        List<String> classUid2 = new ArrayList<>();
        for(int i = 0; i < Infolist1.size();i++){
            a = Analyze.ToRegexHtml(Infolist1.get(i),"<td>","</td>",2);
            for(int j = 0; j < a.size();j++){
                b = Analyze.ToRegexHtml(a.get(j),"blank>","</a>",2);
                for(int k = 0;k < b.size();k++){
                    if(b.get(k).equals("")){
                        //不添加
                    }else{
                        classUid1 = Analyze.ToRegexHtml(b.get(k),"","\\(",2);
                        for (String str:classUid1) {
                            classUid2.add(str);
                        }
                    }
                }
            }
        }
        String URL = "http://jwzx.cqupt.edu.cn/jwzxtmp/showBjStu.php?bj=";
        List<String> stuInfo;
        for(int h = 0 ; h <classUid2.size();h++){
            String allInfo = Analyze.getNetResults(URL,classUid2.get(h));
            List<String>  Infolist = Analyze.ToRegexHtml(allInfo,"<tr>","</tr>",2);
            if(Infolist.size()!=0){
                Infolist.remove(0);//去除第一排的类型名字
                List<String>  imglist  = Analyze.ToRegexNormal(allInfo,"http://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?");
                for(int i = 0; i < Infolist.size();i++){

                    System.out.println(imglist.get(i) +
                                    Analyze.ToRegexHtml(Infolist.get(i),"<td>","</td>",2).get(2)+" "+
                                    Analyze.ToRegexHtml(Infolist.get(i),"<td>","</td>",2).get(3)+" "+
                                    Analyze.ToRegexHtml(Infolist.get(i),"<td>","</td>",2).get(6));

                    Analyze.loadImage(imglist.get(i) ,imglist.get(i)+
                            Analyze.ToRegexHtml(Infolist.get(i),"<td>","</td>",2).get(2)+" "+//名字
                            Analyze.ToRegexHtml(Infolist.get(i),"<td>","</td>",2).get(3)+" "+//性别
                                    Analyze.ToRegexHtml(Infolist.get(i),"<td>","</td>",2).get(6)//专业
                            );
                }
            }
        }
    }
}
