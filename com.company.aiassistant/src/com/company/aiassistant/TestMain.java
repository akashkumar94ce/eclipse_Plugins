package com.company.aiassistant;

import java.util.List;

import com.company.aiassistant.ai.AIActionType;
import com.company.aiassistant.ai.AIRequest;
import com.company.aiassistant.ai.AIResponse;
import com.company.aiassistant.ai.AIServiceImpl;
import com.company.aiassistant.project.GeneratedFile;
import com.company.aiassistant.project.MultiFileResponseParser;
import com.company.aiassistant.util.EditorUtil;

public class TestMain {

    public static void main(String[] args) {

        /*try {

            AIRequest request =
                    new AIRequest();

            request.setActionType(
                    AIActionType.CHAT);

            request.setPrompt(
                    "What is Java Stream API?");

            AIResponse response =
                    new AIServiceImpl()
                            .execute(request);

            System.out.println(
                    "Success = "
                    + response.isSuccess());

            System.out.println(
                    "Response = "
                    + response.getResponse());

            System.out.println(
                    "Error = "
                    + response.getErrorMessage());

        } catch (Exception e) {

            e.printStackTrace();
        }*/
    	
    	
    	String response =
    	        "PROJECT_ACTION: CREATE_FILES\n"
    	      + "\n"
    	      + "FILE_START\n"
    	      + "PATH: src/main/java/com/demo/User.java\n"
    	      + "FILE_TYPE: JAVA\n"
    	      + "\n"
    	      + "public class User {}\n"
    	      + "\n"
    	      + "FILE_END";

    	List<GeneratedFile> files =
    	        MultiFileResponseParser.parse(
    	                response);

    	System.out.println(
    	        files.size());
    	
    }
}