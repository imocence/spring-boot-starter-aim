package com.aim.boot;

import com.aim.core.AiMain;
import com.aim.core.model.ApiDoc;
import com.aim.spring.framework.SpringWebFramework;
import com.aim.core.utils.AimUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @AUTO Spring Web入口
 * @Author AIM
 * @DATE 2021/10/21
 */
@RequestMapping("/aim")
public class AimController {

	private Logger log = LoggerFactory.getLogger(AimController.class);

	@Autowired
	private AimProperties aimProperties;

	private static ApiDoc apiDoc;

	@PostConstruct
	public void init() {
		if (!aimProperties.isEnable()) {
			return;
		}

		String path = aimProperties.getSourcePath();
		String reqUrl = aimProperties.getReqUrl();
		String defResult = aimProperties.getDefResult();
		if (AimUtil.isBlank(path)) {
			path = ".";//默认为当前目录
		}

		List<String> paths = Arrays.asList(path.split(","));

		log.debug("starting aim, source path:{}", paths);

		try {
			AiMain aim = new AiMain(paths, new SpringWebFramework());

			Thread thread = new Thread(() -> {
				try {
					aim.setReqUrl(reqUrl);
					aim.setDefResult(defResult);
					apiDoc = aim.resolve();
					HashMap<String, Object> properties = new HashMap<>();
					properties.put("version", aimProperties.getVersion());
					properties.put("title", aimProperties.getTitle());
					apiDoc.setProperties(properties);

					log.info("start up aim");
				} catch (Exception e) {
					log.error("start up aim error", e);
				}
			});
			thread.start();
		} catch (Exception e) {
			log.error("start up aim error", e);
		}
	}

	/**
	 * 跳转到aim接口文档首页
	 */
	@GetMapping
	public String index() {
		return "redirect:/xdoc/index.html";
	}

	/**
	 * 获取所有文档api
	 *
	 * @return 系统所有文档接口的数据(json格式)
	 */
	@ResponseBody
	@RequestMapping("/apis")
	public Object apis() {
		return apiDoc;
	}

	/**
	 * 重新构建文档
	 *
	 * @return 文档页面
	 */
	@GetMapping("/rebuild")
	public String rebuild(HttpServletRequest request) {
		init();
		return "redirect:/xdoc/index.html";
	}
}
