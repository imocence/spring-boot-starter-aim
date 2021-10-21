import com.aim.core.AiMain;
import com.aim.spring.format.HtmlForamt;
import com.aim.spring.format.MarkdownFormat;
import com.aim.spring.framework.SpringWebFramework;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * @Author AIM
 * @Des
 * @DATE 2021/10/21
 */
public class demo {

	public static void main(String[] args) throws Exception {
		buildHtml();
	}

	private static void buildHtml() throws Exception {
		//生成离线的HTML格式的接口文档
		String userDir = System.getProperty("user.dir");
		FileOutputStream out = new FileOutputStream(new File(userDir, "api.html"));
		AiMain aim = new AiMain(userDir + "/src/main/java/com/aim/controller", new SpringWebFramework());
		aim.build(out, new HtmlForamt());
	}

	private static void buildMarkdown() {
		//生成离线的Markdown格式的接口文档
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		String rootDir = System.getProperty("user.dir");
		AiMain aim = new AiMain(rootDir + "/src/main/java/com/aim/controller", new SpringWebFramework());
		aim.build(out, new MarkdownFormat());

		System.out.println(out.toString());
	}
}
