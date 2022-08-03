package com.fontana.log.producer;

import com.alibaba.fastjson.JSON;
import com.fontana.log.producer.producer.RequestLog;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yegenchang
 * @description
 * @date 2022/7/8 17:25
 */
public class RequestLogTest {

  /***
   * 注意：测试前需要将logback-simple.xml 改名为logback.xml
   */

  private Logger logger = LoggerFactory.getLogger("requestLog");

  @Test
  public void test(){
    RequestLog p =new RequestLog();
    p.setHeader("host=mes-workorder-service\n"
        + "x-real-ip=10.42.0.29\n"
        + "x-forwarded-for=10.42.0.29\n"
        + "x-forwarded-proto=http\n"
        + "connection=close\n"
        + "content-length=37\n"
        + "content-type=application/json;charset=UTF-8\n"
        + "factoryid=1\n"
        + "supos-user=%7B%22staffCode%22%3A%22admin%22%2C%22staffName%22%3A%22%E7%AE%A1%E7%90%86%E5%91%98%22%2C%22username%22%3A%22admin%22%7D\n"
        + "tenantid=1\n"
        + "accept=*/*\n"
        + "user-agent=Java/1.8.0_151");
    p.setTenantId("1");
    p.setMethod("POST");
    p.setParameter("text=不知道大家是否了解，国内后端开发最火的语言是什么？还是 Java。但如今，多语言是必然趋势。很多公司在用 Go 作为主要开发语言，PHP 的使用也非常广泛。每种语言的特点不太一样，很多企业会根据业务需要选择一种合适的语言。这时可能会出现多种语言，业务部门觉得用Go 比较好，偏前端的想要 PHP 或者 Node.js，多语言在企业内部越来越普遍。开发人员想用什么语言就用什么语言，但是运维人员就会面临很大的挑战，如多语言环境下的服务治理怎么能统一做等。目前，云原生领域推出了像 Service Mesh 这样的技术去做多语言的服务治理。就整个生态来讲，目前最成熟的后端语言还是 Java，招聘 Java 人才也比较容易，而 Go 也有非常好的增长趋势。未来，企业对于多语言的容忍程度会越来越高。Java 之前在阿里基本处于统治地位，但现在阿里内部也多语言了。阿里收购了非常多的企业，如饿了么、飞猪、高德等，但不可能让所有并购进来的公司都改变编程语言，这是很难的。由于公司并购，阿里内的编程语言已经变得多元化了。企业足够大的话，就一定是多语言的。如果是初创公司或者体量还不够大，语言统一确实能带来便捷。不知道大家是否了解，国内后端开发最火的语言是什么？还是 Java。但如今，多语言是必然趋势。很多公司在用 Go 作为主要开发语言，PHP 的使用也非常广泛。每种语言的特点不太一样，很多企业会根据业务需要选择一种合适的语言。这时可能会出现多种语言，业务部门觉得用Go 比较好，偏前端的想要 PHP 或者 Node.js，多语言在企业内部越来越普遍。开发人员想用什么语言就用什么语言，但是运维人员就会面临很大的挑战，如多语言环境下的服务治理怎么能统一做等。目前，云原生领域推出了像 Service Mesh 这样的技术去做多语言的服务治理。就整个生态来讲，目前最成熟的后端语言还是 Java，招聘 Java 人才也比较容易，而 Go 也有非常好的增长趋势。未来，企业对于多语言的容忍程度会越来越高。Java 之前在阿里基本处于统治地位，但现在阿里内部也多语言了。阿里收购了非常多的企业，如饿了么、飞猪、高德等，但不可能让所有并购进来的公司都改变编程语言，这是很难的。由于公司并购，阿里内的编程语言已经变得多元化了。企业足够大的话，就一定是多语言的。如果是初创公司或者体量还不够大，语言统一确实能带来便捷。不知道大家是否了解，国内后端开发最火的语言是什么？还是 Java。但如今，多语言是必然趋势。很多公司在用 Go 作为主要开发语言，PHP 的使用也非常广泛。每种语言的特点不太一样，很多企业会根据业务需要选择一种合适的语言。这时可能会出现多种语言，业务部门觉得用Go 比较好，偏前端的想要 PHP 或者 Node.js，多语言在企业内部越来越普遍。开发人员想用什么语言就用什么语言，但是运维人员就会面临很大的挑战，如多语言环境下的服务治理怎么能统一做等。目前，云原生领域推出了像 Service Mesh 这样的技术去做多语言的服务治理。就整个生态来讲，目前最成熟的后端语言还是 Java，招聘 Java 人才也比较容易，而 Go 也有非常好的增长趋势。未来，企业对于多语言的容忍程度会越来越高。Java 之前在阿里基本处于统治地位，但现在阿里内部也多语言了。阿里收购了非常多的企业，如饿了么、飞猪、高德等，但不可能让所有并购进来的公司都改变编程语言，这是很难的。由于公司并购，阿里内的编程语言已经变得多元化了。企业足够大的话，就一定是多语言的。如果是初创公司或者体量还不够大，语言统一确实能带来便捷。不知道大家是否了解，国内后端开发最火的语言是什么？还是 Java。但如今，多语言是必然趋势。很多公司在用 Go 作为主要开发语言，PHP 的使用也非常广泛。每种语言的特点不太一样，很多企业会根据业务需要选择一种合适的语言。这时可能会出现多种语言，业务部门觉得用Go 比较好，偏前端的想要 PHP 或者 Node.js，多语言在企业内部越来越普遍。开发人员想用什么语言就用什么语言，但是运维人员就会面临很大的挑战，如多语言环境下的服务治理怎么能统一做等。目前，云原生领域推出了像 Service Mesh 这样的技术去做多语言的服务治理。就整个生态来讲，目前最成熟的后端语言还是 Java，招聘 Java 人才也比较容易，而 Go 也有非常好的增长趋势。未来，企业对于多语言的容忍程度会越来越高。Java 之前在阿里基本处于统治地位，但现在阿里内部也多语言了。阿里收购了非常多的企业，如饿了么、飞猪、高德等，但不可能让所有并购进来的公司都改变编程语言，这是很难的。由于公司并购，阿里内的编程语言已经变得多元化了。企业足够大的话，就一定是多语言的。如果是初创公司或者体量还不够大，语言统一确实能带来便捷。");
    p.setSuposVersion("3.0.000");
    p.setAppVersion("V1.6.0");
    p.setFactoryId("1");
    p.setPath("/test");
    p.setRemoteIP("192.168.9.44");
    p.setRequestTime(8l);
    p.setResponse("从团队建设角度来说，还是需要有懂 K8s 的人。K8s 的学习资料还是非常多的，InfoQ、CNCF 官网、开源社区的官网，还有阿里云都有大量的资料可以让用户使用。最后一个阶段也是很多企业在做的，就是 Refactor，即重构，企业整个应用架构往往发生一些变化，包括 Serverless 化，微服务化等。这个阶段会涉及应用改造，但也才是真正能够让应用侧发挥云优势的时候。企业可以结合自己的特点，选择逐步的云原生化。另外，企业还要看自己的业务类型。现在有一个叫“双态 IT”的理念，就是讲稳态和敏态。稳态是企业内部变化不是很大的业务，对于这类业务，我们建议只需要做 Replatform 就可以，因为它的迭代速度没有那么快，业务改动也不是很大，但需要通过容器化等模式增强它的稳定性和弹性等。而敏态业务还有快速的迭代，这时可能会建议做 Refactor，如微服务化等，这样云原生实战案例集\n"
        + "10\n"
        + "可以提升整个研发效率。\n"
        + "企业要根据自己的业务类型和技术储备等，综合考虑自己云原生化的方式。▶ 云原生体系越来越大，开发人员要学习的东西也越来越多，您有什么学习建议给到大家吗？\n"
        + "我看到有些企业是为了技术而去做云原生，这样最后不一定有好的结果，更多时候还是先从业务价值角度出发考虑要做什么事情，再选择相应的技术。一方面，企业有业务驱动，便会有足够多的资源投入。另一方面，企业在做技术选型和落地的时候会有足够多的实践。从领域来讲，我给大家的建议就是先把基础打好，之后再完善一些生产必备的技能。容器技术是所有的基石，在这之后是一些比较关键的像可观测性、CICD、微服务等企业内部落地真正需要的一些关键技术。");
    p.setStatusCode(200);
    p.setUserId("admin");
    p.setUserName("管理员");

    logger.info(JSON.toJSONString(p));
  }
}
