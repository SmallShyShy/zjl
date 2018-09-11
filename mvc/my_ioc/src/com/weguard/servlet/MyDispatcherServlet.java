package com.weguard.servlet;

import com.ioc.annotation.Controller;
import com.ioc.annotation.ResponseBody;
import com.ioc.utils.ParamUtil;
import com.ioc.annotation.RequestMapping;
import com.weguard.controller.RBuildingZoneAppController;
import com.weguard.entity.RBuildingZoneApp;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

public class MyDispatcherServlet extends HttpServlet{
    private Properties properties=new Properties();
    private List<String> classNames=new ArrayList<String>();
    private Map<String,Object> ioc=new HashMap<String,Object>();
    private Map<String,Object> requestMapping=new HashMap<String,Object>();
    private Map<String,Object> controller=new HashMap<String,Object>();
    @Override
    public void init(ServletConfig config) throws ServletException {
        //加载配置文件
        doLoadConfig(config.getInitParameter("contextConfigLocation"));
        //扫描包
        doScanner(properties.getProperty("package-scan"));
        //将得到的.class文件反射实例化 方法如ioc容器中
        doInstance();
        //将url和method对应上
        initHandleMapping();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            //处理请求
            doDispatch(req,resp);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            resp.getWriter().write("500!! Server Exception");
        }

    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        if(requestMapping.isEmpty()){
            return;
        }
        String url=req.getRequestURI();
        String contextPath=req.getContextPath();
        url=url.replace(contextPath,"").replaceAll("/+","/");
        if(!requestMapping.containsKey(url)) {
            resp.getWriter().write("404 not found");
            return;
        }
        Method method= (Method) requestMapping.get(url);
        //URL对应方法的参数类型列表
      //  Class<?>[] parameterTypes = method.getParameterTypes();

        //请求的参数列表
        Map<String, String[]> parameterMap = req.getParameterMap();

       // Object[] param=new Object[parameterTypes.length];

        List<String> paramAndType=ParamUtil.getParamNameAndTypeByAnnotation(method);

        Object[] param=new Object[paramAndType.size()];
        String[] paramNames=new String[paramAndType.size()];
        String[] types=new String[paramAndType.size()];

        for(int i=0;i<paramAndType.size();i++){
            paramNames[i] = paramAndType.get(i).split(":")[1];
            types[i]=paramAndType.get(i).split(":")[0];
        }
       for(int i=0;i<param.length;i++)
       {
         param[i]=null;
       }
        for(Map.Entry<String,String[]> en:parameterMap.entrySet()){
           for(int i=0;i<paramNames.length;i++){
               if(en.getKey().equals(paramNames[i])) {
                   if(types[i].equals("int")||types[i].equals("Integer")){
                       param[i]=Integer.parseInt(parameterMap.get(en.getKey())[0]);
                   }else if(types[i].equals("boolean")||types[i].equals("Boolean")){
                       param[i]=Boolean.getBoolean(parameterMap.get(en.getKey())[0]);
                   }else {
                       param[i] = parameterMap.get(en.getKey())[0];
                   }
                   continue;
               }

           }
        }

//        for(int i=0;i<param.length;i++){
//            String simpleName = parameterTypes[i].getSimpleName();
//            if(simpleName.equals("HttpServletRequest")){
//                param[i]=req;
//            }
//            if(simpleName.equals("HttpServletResponse")){
//                param[i]=resp;
//            }
//            if(simpleName.equals("String")){
//               for(Map.Entry<String,String[]> en:parameterMap.entrySet()){
//                   String value =Arrays.toString(en.getValue()).replaceAll("\\[|\\]", "").replaceAll(",\\s", ",");
//                   param[i]=value;
//               }
//            }
//        }
        String result= (String) method.invoke(this.controller.get(url),param);
        if(method.isAnnotationPresent(ResponseBody.class)){
            resp.getWriter().write(result);
        }
    }

    private void doScanner(String property) {
        //获取classpath下 com.wegaurd的url
        URL url=this.getClass().getClassLoader().getResource("/"+property.replaceAll("\\.","/"));
        //url.getFile() 获取getResource得到的文件路径
        File f=new File(url.getFile());
        //遍历路径下所有文件
        for(File file:f.listFiles()){
            if(file.isDirectory()){
                //递归读包
                doScanner(property+"."+file.getName());
            }else{
                String className=property+"."+file.getName().replace(".class","");
                classNames.add(className);
            }
        }
    }

    private void doLoadConfig(String contextConfigLocation){
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(contextConfigLocation);
        try {
            properties.load(resourceAsStream);
//            System.out.println(properties.getProperty("package-scan"));
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(resourceAsStream!=null){
                try {
                    resourceAsStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void doInstance() {
        if(classNames.isEmpty()){
            return;
        }
        for(String c:classNames){
            try {
                Class clazz=Class.forName(c);
                String className=clazz.getSimpleName().substring(0,1).toLowerCase()+clazz.getSimpleName().substring(1,clazz.getSimpleName().length());
                //只实例化 有@Controller的类
                if(clazz.isAnnotationPresent(Controller.class)){
                    ioc.put(className,clazz.newInstance());
                }else{
                    continue;
                }
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
    }
    private void initHandleMapping() {
        if(ioc.isEmpty()){
            return;
        }
        for(Map.Entry<String,Object> entry:ioc.entrySet()) {
            Class clazz = entry.getValue().getClass();
            if (!clazz.isAnnotationPresent(Controller.class)) {
                continue;
            }
            String baseUrl="";
            if(clazz.isAnnotationPresent(RequestMapping.class)){
                RequestMapping re= (RequestMapping) clazz.getAnnotation(RequestMapping.class);
                baseUrl=re.value();
            }
            Method[] methods=clazz.getMethods();
            for(Method m:methods){
                if(!m.isAnnotationPresent(RequestMapping.class)){
                    continue;
                }
                String url=m.getDeclaredAnnotation(RequestMapping.class).value();
                url =(baseUrl+"/"+url).replaceAll("/+", "/");  //多个/替换成一个/
                requestMapping.put(url,m);
                try {
                    controller.put(url,clazz.newInstance());
                    System.out.println(url+","+m);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
