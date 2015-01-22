package org.archive.crawler;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author silvasong E-mail:silvasong@campray.com
 * @version 创建时间：2015年1月12日 下午2:28:58
 * 
 */
public class Test {
	public static void main(String arg[]){
		Element element;
		String image = "";
		try {
			Document page = Jsoup.connect("http://www.net-a-porter.com/product/517379/Toga/-").timeout(10000).get();
			/*element = page.getElementById("product-details");
			element.attr("data-pid");
			element.attr("data-name");
			element.attr("data-price");
			element.attr("data-brand");
			element.attr("data-category");
			System.out.println(element.attr("data-pid"));
			System.out.println(element.attr("data-name"));
			System.out.println(element.attr("data-price"));
			System.out.println(element.attr("data-brand"));
			System.out.println(element.attr("data-category"));
			
			element = page.getElementById("editors-notes-content");
			System.out.println(element.text());
			
			element = page.getElementById("thumbnails-container");
			Elements images = element.getElementsByTag("meta");
			
			for(int i=0 ; i<images.size();i++){
				image+=images.get(i).attr("content")+"#";
			}
			System.out.println(image);
			
			System.out.println("http://www.net-a-porter.com/product/517379?Toga/".split("/")[4].substring(0, 6));*/
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
