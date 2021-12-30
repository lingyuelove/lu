package com.luxuryadmin;

import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.entity.op.DaEFenQiKa;
import com.luxuryadmin.entity.shp.ShpUserNumber;
import com.luxuryadmin.enums.pro.EnumProDeliverSource;
import com.luxuryadmin.param.pro.ParamProPageDeliver;
import com.luxuryadmin.service.shp.ShpUserNumberService;
import com.luxuryadmin.vo.pro.VoProDeliverByPage;
import com.sun.javafx.scene.traversal.Algorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.databene.contiperf.PerfTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.luxuryadmin.api.pro.ProDeliverController;
import org.springframework.validation.BindingResult;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootTest
@Slf4j
class ApiApplicationTests {

    @Autowired
    private ShpUserNumberService shpUserNumberService;

    @Test
        //10个线程 执行10次
        //@PerfTest(invocations = 100,threads = 10)
    void contextLoads() {
/*        for (int i = 0; i < 1000; i++) {
                ShpUserNumber shpUserNumber = shpUserNumberService.getShpUserNumberOverId();
                log.info("ShpUserNumber.id: "+shpUserNumber.getId().toString());
        }*/

    }
    public static void main(String[] args) throws NoSuchAlgorithmException {
        //generatorKey();
      /*  try {
            ApiApplicationTests apiApplicationTests = new ApiApplicationTests();
            apiApplicationTests.use(ApiApplicationTests.class.getMethod("b", Class.forName("java.lang.String")));
        } catch (NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
        }*/
        main2();

    }


    public static void generatorKey() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(512);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        System.out.println(publicKey.toString());

    }

    public String a(String name) {
        System.out.println(name + ":a调用");
        return name + ":a调用";
    }

    public String b(String name) {
        System.out.println(name + ":b调用");
        return name + ":b调用";
    }


    public void use(Method method) {
        try {
            ApiApplicationTests apiApplicationTests = new ApiApplicationTests();
            method.invoke(apiApplicationTests, "15112304365");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    public static void main2() {
        List<DaEFenQiKa> tarArr = new ArrayList<>();
        DaEFenQiKa d1 = new DaEFenQiKa();
        d1.setId(1);
        DaEFenQiKa d2 = new DaEFenQiKa();
        d2.setId(2);
        DaEFenQiKa d3 = new DaEFenQiKa();
        d3.setId(3);
        DaEFenQiKa d4 = new DaEFenQiKa();
        d4.setId(4);
        DaEFenQiKa d5 = new DaEFenQiKa();
        d5.setId(5);
        tarArr.add(d1);
        tarArr.add(d2);
        tarArr.add(d3);
        tarArr.add(d4);
        tarArr.add(d5);


        List<List<DaEFenQiKa>> result = createList(tarArr, 7);

        for (List<DaEFenQiKa> subArr : result) {
            for (DaEFenQiKa str : subArr) {
                System.out.println(str.getId());
            }
        }

    }

    public static List<List<DaEFenQiKa>> createList(List<DaEFenQiKa> targe, int size) {
        List<List<DaEFenQiKa>> listArr = new ArrayList<List<DaEFenQiKa>>();
        //获取被拆分的数组个数
        int arrSize = targe.size() % size == 0 ? targe.size() / size : targe.size() / size + 1;
        for (int i = 0; i < arrSize; i++) {
            List<DaEFenQiKa> sub = new ArrayList<>();
            //把指定索引数据放入到list中
            for (int j = i * size; j <= size * (i + 1) - 1; j++) {
                if (j <= targe.size() - 1) {
                    sub.add((DaEFenQiKa) targe.get(j));
                }
            }
            listArr.add(sub);
        }
        return listArr;
    }
}


