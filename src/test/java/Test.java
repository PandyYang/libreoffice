import com.PreviewApplication;
import com.preview.util.FileUtil;
import org.jodconverter.DocumentConverter;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = PreviewApplication.class)
@RunWith(SpringRunner.class)
public class Test {

    @org.junit.Test
    public void test() {
        FileUtil.reName("I:\\BaiduNetdiskDownload\\123", ".rtf", ".epub");
        System.out.println("123");
    }
}
