package com.skhu.cloud.advice;

import com.skhu.cloud.dto.CustomException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(value = CustomException.class)
    public ModelAndView customException(CustomException e, HttpServletRequest request){
        System.out.println("요것은 CustomException");
        System.out.println("!!!  " + request.getRequestURI());
        ModelAndView view = new ModelAndView();
        view.setViewName("alert");
        view.addObject("msg","다운로드할 파일을 선택해주세요");
        view.addObject("url","/directories?path=" + e.getPath());
        System.out.println("path : " + e.getPath());

        return view;
    }
}
