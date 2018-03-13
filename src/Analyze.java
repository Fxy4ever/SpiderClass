import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Analyze {

    private static int INCLUDE = 1;
    private static int EXCEPT = 2;

    private static String getInfoDuringString(String start,String end){//取得两个html编号之间的数据
        return start + "[\\s\\S]*?" + end;
    }
    private static String getInfoExceptString(String start,String end){//取得不含html标签内的东西
        return "(?<=" + start + ")[\\s\\S]*?(?=" + end + ")";
    }
    public static String getNetResults(String url,String uid){//最开始用这个解析到全部数据
        return ConnectionUtil.Connetc(url+uid);
    }

    public static List<String>  ToRegexHtml(String resource,String start,String end,int type) {
        List<String> list = new ArrayList<>();
        String regexWay ;

        if(type== INCLUDE) regexWay = getInfoDuringString(start,end);
        else regexWay = getInfoExceptString(start,end);

        Pattern pattern = Pattern.compile(regexWay);
        Matcher matcher = pattern.matcher(resource);
        while(matcher.find()){
            list.add(matcher.group());
        }
        return list;
    }
    public static List<String>  ToRegexNormal(String resource,String regexWay) {
        List<String> list = new ArrayList<>();

        Pattern pattern = Pattern.compile(regexWay);
        Matcher matcher = pattern.matcher(resource);
        while(matcher.find()){
            list.add(matcher.group());
        }
        return list;
    }

    static class ConnectionUtil {
        public static String Connetc(String address){
            HttpURLConnection conn = null;
            URL url = null;
            InputStream in = null;
            BufferedReader bufferedReader = null;
            StringBuffer stringBuffer = null;
            try {
                url = new URL(address);
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                conn.setDoInput(true);
                conn.connect();
                in = conn.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(in,"utf-8"));
                stringBuffer = new StringBuffer();
                String line = null;
                while((line = bufferedReader.readLine())!=null){
                    stringBuffer.append(line);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    in.close();
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return stringBuffer.toString();
        }
    }
    public static void loadImage(String URL1){
        URL url = null;
        try {
            url = new URL(URL1);
            DataInputStream dis = new DataInputStream(url.openStream());
            int n  = URL1.indexOf("xh=");
            String imageName = "/Users/mac/Documents/学生照片/"+URL1.substring(n+3);

            FileOutputStream fos = new FileOutputStream(new File(imageName));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length ;
            while((length = dis.read(buffer))>0){
                baos.write(buffer,0,length);
            }
            fos.write(baos.toByteArray());
            dis.close();
            fos.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
