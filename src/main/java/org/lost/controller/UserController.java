package org.lost.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.lost.domain.*;
import org.lost.service.UserService;
import org.lost.util.RandomCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @RequestMapping("/checkCode")
    @ResponseBody
    public void checkCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //服务器通知浏览器不要缓存
        response.setHeader("pragma","no-cache");
        response.setHeader("cache-control","no-cache");
        response.setHeader("expires","0");

        //在内存中创建一个长80，宽30的图片，默认黑色背景
        //参数一：长
        //参数二：宽
        //参数三：颜色
        int width = 80;
        int height = 30;
        BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);

        //获取画笔
        Graphics g = image.getGraphics();
        //设置画笔颜色为灰色
        g.setColor(Color.GRAY);
        //填充图片
        g.fillRect(0,0, width,height);

        //产生4个随机验证码
        String checkCode = RandomCode.getCheckCode();
        //将验证码放入HttpSession中
        request.getSession().setAttribute("CHECKCODE_SERVER",checkCode);

        //设置画笔颜色为黄色
        g.setColor(Color.YELLOW);
        //设置字体的小大
        g.setFont(new Font("黑体",Font.BOLD,24));
        //向图片上写入验证码
        g.drawString(checkCode,15,25);

        //将内存中的图片输出到浏览器
        //参数一：图片对象
        //参数二：图片的格式，如PNG,JPG,GIF
        //参数三：图片输出到哪里去
        ImageIO.write(image,"PNG",response.getOutputStream());
    }

    @RequestMapping("/register")
    @ResponseBody
    public ResultInfo register(RUser user,HttpServletRequest request, HttpServletResponse response) throws IOException {
        ResultInfo info = new ResultInfo();
        try {
            // 如果注册成功，不抛出抛出异常，如果注册失败抛出异常
            String check = user.getCheck();
            HttpSession session = request.getSession();
            String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
            session.removeAttribute("CHECKCODE_SERVER");
            boolean u = userService.findByName(user.getName());
            response.setContentType("application/json;charset=utf-8");
            if(checkcode_server == null || !checkcode_server.equalsIgnoreCase(check)){
                //验证码错误
                //注册失败
                info.setFlag(false);
                info.setErrorMsg("验证码错误");
                return info;
            }else if(!u){
                info.setFlag(false);
                info.setErrorMsg("用户名错误");
                return info;
            } else {
                info.setFlag(true);
                info.setErrorMsg("成功，验证邮箱");
                userService.register(user);
                return info;
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            info.setFlag(false);
            info.setErrorMsg("错误");
            return info;
        }
    }

    @RequestMapping("/active")
    public String active(String code){
        boolean check = userService.active(code);
        if(check){
            return "redirect:/login.html";
        }
        return "redirect:/error.html";
    }

    @RequestMapping("/login")
    @ResponseBody
    public ResultInfo login(LUser user,HttpServletRequest request, HttpServletResponse response) throws IOException {
        ResultInfo info = new ResultInfo();
        try {
            // 如果注册成功，不抛出抛出异常，如果注册失败抛出异常
            String check = user.getCheck();
            HttpSession session = request.getSession();
            String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
            session.removeAttribute("CHECKCODE_SERVER");
            User u = userService.login(user);
            response.setContentType("application/json;charset=utf-8");
            if(checkcode_server == null || !checkcode_server.equalsIgnoreCase(check)){
                //验证码错误
                //注册失败
                return new ResultInfo(false,"验证码错误");
            }else if(u != null){
                Cookie cookie1 = new Cookie("loginId",u.getId());
                cookie1.setPath("/");
                Cookie cookie2 = new Cookie("loginPw",u.getPassword());
                cookie2.setPath("/");
                response.addCookie(cookie1);
                response.addCookie(cookie2);
                //登录成功
                return new ResultInfo(true,"成功");
            } else {
                return new ResultInfo(false,"用户名或密码错误");
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new ResultInfo(false,"错误");
        }
    }

    @RequestMapping("/upload")
    @ResponseBody
    public ResultInfo upload(@RequestParam("pic") MultipartFile file,@RequestParam("loginId") String id,@RequestParam("loginPw") String password){
        ResultInfo info = new ResultInfo();

        try {
            User user = userService.upload(file, id, password);
            if(user != null) {
                info.setFlag(true);
                info.setRe(user.getSmallPic());
                info.setErrorMsg("头像上传成功");
                return new ResultInfo(true,user.getSmallPic(),"头像上传成功");
            }
            else {
                return new ResultInfo(false,"错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultInfo(false,"错误");
        }
    }

    @RequestMapping("/detail")
    @ResponseBody
    public User detail(@RequestParam("loginId") String id,@RequestParam("loginPw") String password){
        try {
            User user = userService.selectByPrimaryKey(id, password);
            return user;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping("/saveDetail")
    @ResponseBody
    public ResultInfo userDetail(@RequestParam("id") String id,@RequestParam("name") String name,@RequestParam("password") String password,@RequestParam("email") String email,@RequestParam("oldPassword") String oldPassword){
        NUser user = new NUser(id,name,password,email,oldPassword);
        try {
            boolean u = userService.changeUser(user);
            if(u) {
                return new ResultInfo(true, "保存成功,如更改密码请重新登录");
            }else return new ResultInfo(false,"错误");
        }catch (Exception e){
            e.printStackTrace();
            return new ResultInfo(false,"错误");
        }
    }


}
