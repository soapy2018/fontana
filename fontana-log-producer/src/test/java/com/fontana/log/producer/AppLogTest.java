package com.fontana.log.producer;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yegenchang
 * @description
 * @date 2022/6/21 19:31
 */
public class AppLogTest {

  /***
   * 注意：测试前需要将logback-simple.xml 改名为logback.xml
   */

  private Logger logger = LoggerFactory.getLogger(AppLogTest.class);


  @Test
  public void info() {
    for (int i = 0; i < 5; i++) {
      logger.info("info 日志 单元测试改造就可以使用 Serverless 架构，也不用关心底层的 IaaS 基础设施。此外，还有面向 K8s的 Serverless 产品，用户可以通过 K8s 的界面使用 Serverless，还有面向容器的 Serverless，即用来交付容器实例的 Serverless。这些产品的本质都是让使用者以一个界面使用云资源，而不必关心底下的基础设施。Serverless 多样化给用户带来了更多的选择。还有一个比较大的趋势就是越来越多的云产品也在变得 Serverless 化。如果大家关注了亚马逊的 re:Invent 就会发现，很多云产品本身也在 Serverless 化，比如推出了 Kafka 的 Serverless 版本。这意味着，用户在实际使用云产品时，完全不需要关注云产品本身的规模，直接按照按量付费即可。\n"
          + "云产品本身的 Serverless 化也带来了多样性，在我看来，这个多样性是 Serverless 理念在用户使用界面和产品形态上不断丰富带来的，也在不断推动行业的标准化进程，用户在使用多种多样的 Serverless 产品时，也能用标准的形式去使用各个云厂商的产品。比如函数计算领域，它的触发会是越来越标准的 http 模式，可观测性能够与 Prometheus、OpenTelemetry 等开源技术结合，这些都会让 Serverless 产品标准化程度也越来越高。用户需求的多样化是与 Serverless 产品的标准化结合在一起的，并且这是一个必经的过程，这样才能有越来越多的用户使用。改造就可以使用 Serverless 架构，也不用关心底层的 IaaS 基础设施。此外，还有面向 K8s的 Serverless 产品，用户可以通过 K8s 的界面使用 Serverless，还有面向容器的 Serverless，即用来交付容器实例的 Serverless。这些产品的本质都是让使用者以一个界面使用云资源，而不必关心底下的基础设施。Serverless 多样化给用户带来了更多的选择。还有一个比较大的趋势就是越来越多的云产品也在变得 Serverless 化。如果大家关注了亚马逊的 re:Invent 就会发现，很多云产品本身也在 Serverless 化，比如推出了 Kafka 的 Serverless 版本。这意味着，用户在实际使用云产品时，完全不需要关注云产品本身的规模，直接按照按量付费即可。\n"
          + "云产品本身的 Serverless 化也带来了多样性，在我看来，这个多样性是 Serverless 理念在用户使用界面和产品形态上不断丰富带来的，也在不断推动行业的标准化进程，用户在使用多种多样的 Serverless 产品时，也能用标准的形式去使用各个云厂商的产品。比如函数计算领域，它的触发会是越来越标准的 http 模式，可观测性能够与 Prometheus、OpenTelemetry 等开源技术结合，这些都会让 Serverless 产品标准化程度也越来越高。用户需求的多样化是与 Serverless 产品的标准化结合在一起的，并且这是一个必经的过程，这样才能有越来越多的用户使用。改造就可以使用 Serverless 架构，也不用关心底层的 IaaS 基础设施。此外，还有面向 K8s的 Serverless 产品，用户可以通过 K8s 的界面使用 Serverless，还有面向容器的 Serverless，即用来交付容器实例的 Serverless。这些产品的本质都是让使用者以一个界面使用云资源，而不必关心底下的基础设施。Serverless 多样化给用户带来了更多的选择。还有一个比较大的趋势就是越来越多的云产品也在变得 Serverless 化。如果大家关注了亚马逊的 re:Invent 就会发现，很多云产品本身也在 Serverless 化，比如推出了 Kafka 的 Serverless 版本。这意味着，用户在实际使用云产品时，完全不需要关注云产品本身的规模，直接按照按量付费即可。\n"
          + "云产品本身的 Serverless 化也带来了多样性，在我看来，这个多样性是 Serverless 理念在用户使用界面和产品形态上不断丰富带来的，也在不断推动行业的标准化进程，用户在使用多种多样的 Serverless 产品时，也能用标准的形式去使用各个云厂商的产品。比如函数计算领域，它的触发会是越来越标准的 http 模式，可观测性能够与 Prometheus、OpenTelemetry 等开源技术结合，这些都会让 Serverless 产品标准化程度也越来越高。用户需求的多样化是与 Serverless 产品的标准化结合在一起的，并且这是一个必经的过程，这样才能有越来越多的用户使用。改造就可以使用 Serverless 架构，也不用关心底层的 IaaS 基础设施。此外，还有面向 K8s的 Serverless 产品，用户可以通过 K8s 的界面使用 Serverless，还有面向容器的 Serverless，即用来交付容器实例的 Serverless。这些产品的本质都是让使用者以一个界面使用云资源，而不必关心底下的基础设施。Serverless 多样化给用户带来了更多的选择。还有一个比较大的趋势就是越来越多的云产品也在变得 Serverless 化。如果大家关注了亚马逊的 re:Invent 就会发现，很多云产品本身也在 Serverless 化，比如推出了 Kafka 的 Serverless 版本。这意味着，用户在实际使用云产品时，完全不需要关注云产品本身的规模，直接按照按量付费即可。\n"
          + "云产品本身的 Serverless 化也带来了多样性，在我看来，这个多样性是 Serverless 理念在用户使用界面和产品形态上不断丰富带来的，也在不断推动行业的标准化进程，用户在使用多种多样的 Serverless 产品时，也能用标准的形式去使用各个云厂商的产品。比如函数计算领域，它的触发会是越来越标准的 http 模式，可观测性能够与 Prometheus、OpenTelemetry 等开源技术结合，这些都会让 Serverless 产品标准化程度也越来越高。用户需求的多样化是与 Serverless 产品的标准化结合在一起的，并且这是一个必经的过程，这样才能有越来越多的用户使用。改造就可以使用 Serverless 架构，也不用关心底层的 IaaS 基础设施。此外，还有面向 K8s的 Serverless 产品，用户可以通过 K8s 的界面使用 Serverless，还有面向容器的 Serverless，即用来交付容器实例的 Serverless。这些产品的本质都是让使用者以一个界面使用云资源，而不必关心底下的基础设施。Serverless 多样化给用户带来了更多的选择。还有一个比较大的趋势就是越来越多的云产品也在变得 Serverless 化。如果大家关注了亚马逊的 re:Invent 就会发现，很多云产品本身也在 Serverless 化，比如推出了 Kafka 的 Serverless 版本。这意味着，用户在实际使用云产品时，完全不需要关注云产品本身的规模，直接按照按量付费即可。\n"
          + "云产品本身的 Serverless 化也带来了多样性，在我看来，这个多样性是 Serverless 理念在用户使用界面和产品形态上不断丰富带来的，也在不断推动行业的标准化进程，用户在使用多种多样的 Serverless 产品时，也能用标准的形式去使用各个云厂商的产品。比如函数计算领域，它的触发会是越来越标准的 http 模式，可观测性能够与 Prometheus、OpenTelemetry 等开源技术结合，这些都会让 Serverless 产品标准化程度也越来越高。用户需求的多样化是与 Serverless 产品的标准化结合在一起的，并且这是一个必经的过程，这样才能有越来越多的用户使用。改造就可以使用 Serverless 架构，也不用关心底层的 IaaS 基础设施。此外，还有面向 K8s的 Serverless 产品，用户可以通过 K8s 的界面使用 Serverless，还有面向容器的 Serverless，即用来交付容器实例的 Serverless。这些产品的本质都是让使用者以一个界面使用云资源，而不必关心底下的基础设施。Serverless 多样化给用户带来了更多的选择。还有一个比较大的趋势就是越来越多的云产品也在变得 Serverless 化。如果大家关注了亚马逊的 re:Invent 就会发现，很多云产品本身也在 Serverless 化，比如推出了 Kafka 的 Serverless 版本。这意味着，用户在实际使用云产品时，完全不需要关注云产品本身的规模，直接按照按量付费即可。\n"
          + "云产品本身的 Serverless 化也带来了多样性，在我看来，这个多样性是 Serverless 理念在用户使用界面和产品形态上不断丰富带来的，也在不断推动行业的标准化进程，用户在使用多种多样的 Serverless 产品时，也能用标准的形式去使用各个云厂商的产品。比如函数计算领域，它的触发会是越来越标准的 http 模式，可观测性能够与 Prometheus、OpenTelemetry 等开源技术结合，这些都会让 Serverless 产品标准化程度也越来越高。用户需求的多样化是与 Serverless 产品的标准化结合在一起的，并且这是一个必经的过程，这样才能有越来越多的用户使用。改造就可以使用 Serverless 架构，也不用关心底层的 IaaS 基础设施。此外，还有面向 K8s的 Serverless 产品，用户可以通过 K8s 的界面使用 Serverless，还有面向容器的 Serverless，即用来交付容器实例的 Serverless。这些产品的本质都是让使用者以一个界面使用云资源，而不必关心底下的基础设施。Serverless 多样化给用户带来了更多的选择。还有一个比较大的趋势就是越来越多的云产品也在变得 Serverless 化。如果大家关注了亚马逊的 re:Invent 就会发现，很多云产品本身也在 Serverless 化，比如推出了 Kafka 的 Serverless 版本。这意味着，用户在实际使用云产品时，完全不需要关注云产品本身的规模，直接按照按量付费即可。\n"
          + "云产品本身的 Serverless 化也带来了多样性，在我看来，这个多样性是 Serverless 理念在用户使用界面和产品形态上不断丰富带来的，也在不断推动行业的标准化进程，用户在使用多种多样的 Serverless 产品时，也能用标准的形式去使用各个云厂商的产品。比如函数计算领域，它的触发会是越来越标准的 http 模式，可观测性能够与 Prometheus、OpenTelemetry 等开源技术结合，这些都会让 Serverless 产品标准化程度也越来越高。用户需求的多样化是与 Serverless 产品的标准化结合在一起的，并且这是一个必经的过程，这样才能有越来越多的用户使用。改造就可以使用 Serverless 架构，也不用关心底层的 IaaS 基础设施。此外，还有面向 K8s的 Serverless 产品，用户可以通过 K8s 的界面使用 Serverless，还有面向容器的 Serverless，即用来交付容器实例的 Serverless。这些产品的本质都是让使用者以一个界面使用云资源，而不必关心底下的基础设施。Serverless 多样化给用户带来了更多的选择。还有一个比较大的趋势就是越来越多的云产品也在变得 Serverless 化。如果大家关注了亚马逊的 re:Invent 就会发现，很多云产品本身也在 Serverless 化，比如推出了 Kafka 的 Serverless 版本。这意味着，用户在实际使用云产品时，完全不需要关注云产品本身的规模，直接按照按量付费即可。\n"
          + "云产品本身的 Serverless 化也带来了多样性，在我看来，这个多样性是 Serverless 理念在用户使用界面和产品形态上不断丰富带来的，也在不断推动行业的标准化进程，用户在使用多种多样的 Serverless 产品时，也能用标准的形式去使用各个云厂商的产品。比如函数计算领域，它的触发会是越来越标准的 http 模式，可观测性能够与 Prometheus、OpenTelemetry 等开源技术结合，这些都会让 Serverless 产品标准化程度也越来越高。用户需求的多样化是与 Serverless 产品的标准化结合在一起的，并且这是一个必经的过程，这样才能有越来越多的用户使用。改造就可以使用 Serverless 架构，也不用关心底层的 IaaS 基础设施。此外，还有面向 K8s的 Serverless 产品，用户可以通过 K8s 的界面使用 Serverless，还有面向容器的 Serverless，即用来交付容器实例的 Serverless。这些产品的本质都是让使用者以一个界面使用云资源，而不必关心底下的基础设施。Serverless 多样化给用户带来了更多的选择。还有一个比较大的趋势就是越来越多的云产品也在变得 Serverless 化。如果大家关注了亚马逊的 re:Invent 就会发现，很多云产品本身也在 Serverless 化，比如推出了 Kafka 的 Serverless 版本。这意味着，用户在实际使用云产品时，完全不需要关注云产品本身的规模，直接按照按量付费即可。\n"
          + "云产品本身的 Serverless 化也带来了多样性，在我看来，这个多样性是 Serverless 理念在用户使用界面和产品形态上不断丰富带来的，也在不断推动行业的标准化进程，用户在使用多种多样的 Serverless 产品时，也能用标准的形式去使用各个云厂商的产品。比如函数计算领域，它的触发会是越来越标准的 http 模式，可观测性能够与 Prometheus、OpenTelemetry 等开源技术结合，这些都会让 Serverless 产品标准化程度也越来越高。用户需求的多样化是与 Serverless 产品的标准化结合在一起的，并且这是一个必经的过程，这样才能有越来越多的用户使用。改造就可以使用 Serverless 架构，也不用关心底层的 IaaS 基础设施。此外，还有面向 K8s的 Serverless 产品，用户可以通过 K8s 的界面使用 Serverless，还有面向容器的 Serverless，即用来交付容器实例的 Serverless。这些产品的本质都是让使用者以一个界面使用云资源，而不必关心底下的基础设施。Serverless 多样化给用户带来了更多的选择。还有一个比较大的趋势就是越来越多的云产品也在变得 Serverless 化。如果大家关注了亚马逊的 re:Invent 就会发现，很多云产品本身也在 Serverless 化，比如推出了 Kafka 的 Serverless 版本。这意味着，用户在实际使用云产品时，完全不需要关注云产品本身的规模，直接按照按量付费即可。\n"
          + "云产品本身的 Serverless 化也带来了多样性，在我看来，这个多样性是 Serverless 理念在用户使用界面和产品形态上不断丰富带来的，也在不断推动行业的标准化进程，用户在使用多种多样的 Serverless 产品时，也能用标准的形式去使用各个云厂商的产品。比如函数计算领域，它的触发会是越来越标准的 http 模式，可观测性能够与 Prometheus、OpenTelemetry 等开源技术结合，这些都会让 Serverless 产品标准化程度也越来越高。用户需求的多样化是与 Serverless 产品的标准化结合在一起的，并且这是一个必经的过程，这样才能有越来越多的用户使用。改造就可以使用 Serverless 架构，也不用关心底层的 IaaS 基础设施。此外，还有面向 K8s的 Serverless 产品，用户可以通过 K8s 的界面使用 Serverless，还有面向容器的 Serverless，即用来交付容器实例的 Serverless。这些产品的本质都是让使用者以一个界面使用云资源，而不必关心底下的基础设施。Serverless 多样化给用户带来了更多的选择。还有一个比较大的趋势就是越来越多的云产品也在变得 Serverless 化。如果大家关注了亚马逊的 re:Invent 就会发现，很多云产品本身也在 Serverless 化，比如推出了 Kafka 的 Serverless 版本。这意味着，用户在实际使用云产品时，完全不需要关注云产品本身的规模，直接按照按量付费即可。\n"
          + "云产品本身的 Serverless 化也带来了多样性，在我看来，这个多样性是 Serverless 理念在用户使用界面和产品形态上不断丰富带来的，也在不断推动行业的标准化进程，用户在使用多种多样的 Serverless 产品时，也能用标准的形式去使用各个云厂商的产品。比如函数计算领域，它的触发会是越来越标准的 http 模式，可观测性能够与 Prometheus、OpenTelemetry 等开源技术结合，这些都会让 Serverless 产品标准化程度也越来越高。用户需求的多样化是与 Serverless 产品的标准化结合在一起的，并且这是一个必经的过程，这样才能有越来越多的用户使用。改造就可以使用 Serverless 架构，也不用关心底层的 IaaS 基础设施。此外，还有面向 K8s的 Serverless 产品，用户可以通过 K8s 的界面使用 Serverless，还有面向容器的 Serverless，即用来交付容器实例的 Serverless。这些产品的本质都是让使用者以一个界面使用云资源，而不必关心底下的基础设施。Serverless 多样化给用户带来了更多的选择。还有一个比较大的趋势就是越来越多的云产品也在变得 Serverless 化。如果大家关注了亚马逊的 re:Invent 就会发现，很多云产品本身也在 Serverless 化，比如推出了 Kafka 的 Serverless 版本。这意味着，用户在实际使用云产品时，完全不需要关注云产品本身的规模，直接按照按量付费即可。\n"
          + "云产品本身的 Serverless 化也带来了多样性，在我看来，这个多样性是 Serverless 理念在用户使用界面和产品形态上不断丰富带来的，也在不断推动行业的标准化进程，用户在使用多种多样的 Serverless 产品时，也能用标准的形式去使用各个云厂商的产品。比如函数计算领域，它的触发会是越来越标准的 http 模式，可观测性能够与 Prometheus、OpenTelemetry 等开源技术结合，这些都会让 Serverless 产品标准化程度也越来越高。用户需求的多样化是与 Serverless 产品的标准化结合在一起的，并且这是一个必经的过程，这样才能有越来越多的用户使用。 " + i);
    }
    try {
      //由于推送日志是异步执行，这里睡眠一下
      Thread.sleep(1000);
    }catch (Exception e){

    }
  }
}
