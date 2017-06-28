package com.ecommerce.testassistant;



import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import com.ecommerce.testassistant.Constants;

import static com.ecommerce.testassistant.GetOSName.OsUtils.*;

import static com.ecommerce.test.DriverScript.APP_LOGS;
import static com.ecommerce.test.DriverScript.CONFIG;
import static com.ecommerce.test.DriverScript.OR;
@SuppressWarnings("ALL")
public class Keywords {
	public WebDriver driver;
	
	
	public String openBrowser(String object,String data){

        // Chrome Driver Path

            System.setProperty("webdriver.chrome.driver", "ChromeDriver/chromedriver");



        // Internet Explorer Path
        if (isWindows()) {
            File file = new File("IEDriver/IEDriver.exe");
            System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
        }

        APP_LOGS.debug("Opening browser");
        if(data.equals("Mozilla"))
            driver=new FirefoxDriver();
        else if(data.equals("IE"))
            driver=new InternetExplorerDriver();
        else if(data.equals("Chrome"))
            driver=new ChromeDriver();

        long implicitWaitTime=Long.parseLong(CONFIG.getProperty("implicitwait"));
        driver.manage().timeouts().implicitlyWait(implicitWaitTime, TimeUnit.SECONDS);
        return Constants.KEYWORD_PASS;

    }
	
    public String navigate(String object,String data){
        APP_LOGS.debug("Navigating to URL");
        try{
            driver.navigate().to(data);
        }catch(Exception e){
            return Constants.KEYWORD_FAIL+" -- Not able to navigate";
        }
        return Constants.KEYWORD_PASS;
    }
    public String clickLink_xpath(String object,String data){
        APP_LOGS.debug("Clicking on link ");
        try{
            driver.findElement(By.xpath(OR.getProperty(object))).click();
        }catch(Exception e){
            return Constants.KEYWORD_FAIL+" -- Not able to click on link"+e.getMessage();
        }

        return Constants.KEYWORD_PASS;
    }
    
    public void captureScreenshot(String filename, String keyword_execution_result) throws IOException{
        // take screen shots
        if(CONFIG.getProperty("screenshot_everystep").equals("Y")){
            // capturescreen

            File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(scrFile, new File(System.getProperty("user.dir") +"//screenshots//"+filename+".jpg"));

        }else if (keyword_execution_result.startsWith(Constants.KEYWORD_FAIL) && CONFIG.getProperty("screenshot_error").equals("Y") ){
            // capture screenshot
            File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(scrFile, new File(System.getProperty("user.dir") +"//screenshots//"+filename+".jpg"));
        }
    }
    
    public  String closeBrowser(String object, String data){
        APP_LOGS.debug("Closing the browser");
        try{
            driver.close();
        }catch(Exception e){
            return Constants.KEYWORD_FAIL+"Unable to close browser. Check if its open"+e.getMessage();
        }
        return Constants.KEYWORD_PASS;

    }


}
