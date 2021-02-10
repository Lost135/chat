package org.lost.service.impl;

import org.apache.commons.io.FileUtils;
import org.lost.domain.LUser;
import org.lost.domain.NUser;
import org.lost.domain.RUser;
import org.lost.domain.User;
import org.lost.mapper.UserMapper;
import org.lost.service.UserService;
import org.lost.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private IdWorker idWorker;
    @Autowired
    private Environment env;
    @Autowired
    private QRCodeUtils qrCodeUtils;
    @Autowired
    private UserMapper userMapper;

    @Override
    public void register(RUser rUser) {
        try {
            User user = new User();
            // 将用户信息保存到数据库中
            // 使用雪花算法来生成唯一ID
            user.setId(idWorker.nextId());
            user.setName(rUser.getName());
            // 对密码进行MD5加密
            user.setPassword(DigestUtils.md5DigestAsHex(rUser.getPassword().getBytes()));
            user.setSmallPic("");
            user.setBigPic("");
            user.setStatus("N");
            user.setCode(UuidUtil.getUuid());
            user.setEmail(rUser.getEmail());
/**
            // 生成二维码，并且将二维码的路径保存到数据库中
            // 要生成二维码中的字符串
            String qrcodeStr = "hichat://" + user.getName();
            // 获取一个临时目录，用来保存临时的二维码图片
            String tempDir = env.getProperty("hcat.tmpdir");
            String qrCodeFilePath = tempDir + user.getName() + ".png";
            qrCodeUtils.createQRCode(qrCodeFilePath, qrcodeStr);

            // 将临时保存的二维码上传到FastDFS
            String url = env.getProperty("fdfs.httpurl") +
                    fastDFSClient.uploadFile(new File(qrCodeFilePath));
**/
            user.setQrcode("");

            userMapper.register(user);
            String content="<a href='http://localhost:8080/user/active?code="+user.getCode()+"'>点击激活</a>";

            MailUtils.sendMail(user.getEmail(),content,"激活邮件");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("注册失败");
        }
    }

    /**
     * 激活用户
     * @param code
     * @return
     */
    @Override
    public boolean active(String code) {
        User user = userMapper.findByCode(code);
        if (user != null){
            userMapper.updateStatus(user.getId());
            return true;
        }else {
            return false;
        }

    }

    @Override
    public User login(LUser user) {
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        User u = userMapper.login(user);
        return u;
    }

    @Override
    public boolean findByName(String name) {
        User u = userMapper.findByName(name);
        if (u == null){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public User upload(MultipartFile file, String id, String password) {
        try {
            // 获取文件名称,包含后缀
            String fileName = id + ".png";
            // 存放在这个路径下：该路径是该工程目录下的static文件下：(注：该文件可能需要自己创建)
            // 放在static下的原因是，存放的是静态文件资源，即通过浏览器输入本地服务器地址，加文件名时是可以访问到的

            String path = env.getProperty("img.path");

        User user = new User();
            user.setId(id);
            user.setPassword(password);
            String url = env.getProperty("img.url") + fileName;
            // 设置头像大图片
            user.setBigPic(url);
            // 设置头像小图片
            user.setSmallPic(url);
            // 将新的头像URL更新到数据库中
            userMapper.updatePic(user);
            User u = userMapper.selectByPrimaryKey(id,password);
            if(u != null){
                //剪裁并保存图片
                ImgUtil.scale(file,path + fileName,250,250,true);
                return user;
            }else return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public User selectByPrimaryKey(String id, String password) {
        User user = userMapper.selectByPrimaryKey(id,password);
        return user;
    }

    @Override
    public boolean changeUser(NUser user) {
        try{
            user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
            userMapper.changeUser(user);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
