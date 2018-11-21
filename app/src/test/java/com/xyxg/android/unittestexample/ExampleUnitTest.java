package com.xyxg.android.unittestexample;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @BeforeClass
    public static void setUpClass() {
        // 在该测试类中只执行一次
        System.out.println("@BeforeClass - executed only one time and is first method to be executed");

        String s = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "\t<head>\n" +
                "\t\t<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\"\n" +
                "\t\t/>\n" +
                "\t\t<title>\n" +
                "\t\t\tnetease mail\n" +
                "\t\t</title>\n" +
                "\t</head>\n" +
                "\t<body>\n" +
                "\t\t<div>\n" +
                "\t\t\t<p style=\"font-size:15px\">\n" +
                "\t\t\t\t<br/>\n" +
                "\t\t\t\t<br/>\n" +
                "\t\t\t\t??????let2575\n" +
                "\t\t\t</p>\n" +
                "\t\t</div>\n" +
                "\t\t<style type=\"text/css\">\n" +
                "\t\t\t.netease-attDown{ width:430px;height:auto;background-color:#F6F9FC;border:#C5D2DA\n" +
                "\t\t\t1px solid;padding:12px;line-height:160%;font-size:12px;white-space:nowrap;font-family:Verdana}\n" +
                "\t\t\t.netease-attDown-tit{ color:#A0A0A0 } .netease-attDown-tit strong{ color:#008000\n" +
                "\t\t\t} .netease-attDown a{ color:#0669B2} .netease-attDown dl, .netease-attDown\n" +
                "\t\t\tdt, .netease-attDown dd{ margin:0;padding:0;width:100%;white-space:nowrap;overflow:hidden;text-overflow:ellipsis}\n" +
                "\t\t\t.netease-attDown dt{ font-weight:bold;color:#333;margin-top:8px} .netease-attDown\n" +
                "\t\t\tdt .info{ color:#999;font-weight:normal}\n" +
                "\t\t</style>\n" +
                "\t\t<div class=\"netease-attDown\">\n" +
                "\t\t\t<div class=\"netease-attDown-tit\">\n" +
                "\t\t\t\t<strong>\n" +
                "\t\t\t\t\t2\n" +
                "\t\t\t\t</strong>\n" +
                "\t\t\t\tattachments\n" +
                "\t\t\t</div>\n" +
                "\t\t\t<dl>\n" +
                "\t\t\t\t<dt title=\"uuid.bck\">\n" +
                "\t\t\t\t\tuuid.bck\n" +
                "\t\t\t\t\t<span class=\"info\">\n" +
                "\t\t\t\t\t\t(1K)\n" +
                "\t\t\t\t\t</span>\n" +
                "\t\t\t\t</dt>\n" +
                "\t\t\t\t<dd>\n" +
                "\t\t\t\t\t<a href=\"http://preview.mail.126.com/xdownload?filename=uuid.bck&mid=1S2mnRtI%2BVc1qNLpYgAAsk&part=1&sign=86fbdfa6531721cbdbd919e42283b570&time=1482740513&uid=xyxgylm%40126.com\"\n" +
                "\t\t\t\t\ttarget=\"_blank\">\n" +
                "\t\t\t\t\t\tdownload\n" +
                "\t\t\t\t\t</a>\n" +
                "\t\t\t\t</dd>\n" +
                "\t\t\t</dl>\n" +
                "\t\t\t<dl>\n" +
                "\t\t\t\t<dt title=\"IMG20161221231737.jpg\">\n" +
                "\t\t\t\t\tIMG20161221231737.jpg\n" +
                "\t\t\t\t\t<span class=\"info\">\n" +
                "\t\t\t\t\t\t(4M)\n" +
                "\t\t\t\t\t</span>\n" +
                "\t\t\t\t</dt>\n" +
                "\t\t\t\t<dd>\n" +
                "\t\t\t\t\t<a href=\"http://preview.mail.126.com/xdownload?filename=IMG20161221231737.jpg&mid=1S2mnRtI%2BVc1qNLpYgAAsk&part=2&sign=86fbdfa6531721cbdbd919e42283b570&time=1482740513&uid=xyxgylm%40126.com\"\n" +
                "\t\t\t\t\ttarget=\"_blank\">\n" +
                "\t\t\t\t\t\tdownload\n" +
                "\t\t\t\t\t</a>\n" +
                "\t\t\t\t\t<a href=\"http://preview.mail.126.com/preview?mid=1S2mnRtI%2BVc1qNLpYgAAsk&part=2&sign=86fbdfa6531721cbdbd919e42283b570&time=1482740513&uid=xyxgylm%40126.com\"\n" +
                "\t\t\t\t\ttarget=\"_blank\">\n" +
                "\t\t\t\t\t\tpreview\n" +
                "\t\t\t\t\t</a>\n" +
                "\t\t\t\t</dd>\n" +
                "\t\t\t</dl>\n" +
                "\t\t</div>\n" +
                "\t</body>\n" +
                "\n" +
                "</html>";
//        s = s.replaceAll("[<](/)?div[^>]*[>]", "");
        s = s.replaceAll("<div[^>]*(netease\\SattDown\\Stit)[^>]*>[\\s\\S]*?</div>", "");
        s = s.replaceAll("<div[^>]*(netease\\SattDown)[^>]*>[\\s\\S]*?</div>", "");
//        s = s.replaceAll("<div[^>]*>[\\s\\S]*?</div>", "");
        System.out.println(s);
    }
    @AfterClass
    public static void afterClass() {
        // 在该测试类中只执行一次
        System.out.println("@AfterClass - executed only one time and is last method to be executed");
    }
    @Before
    public void setUp() {
        // 在每次执行测试方法前执行
        System.out.println("@Before - executed before every test method");
    }
    @After
    public void after() {
        // 在每次执行测试方法之后执行
        System.out.println("@After - executed after every test method");
    }
    @Test
    public void addition_isCorrect1() throws Exception {
        assertEquals(4, 2 + 2);
        System.out.println("@Test - executed addition_isCorrect1 test method");
    }
    @Test
    public void addition_isCorrect2() throws Exception {
        assertEquals(4, 2 + 2);
        System.out.println("@Test - executed addition_isCorrect2 test method");
    }
}