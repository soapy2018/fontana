package com.fontana.log.producer.producer.producer.internals;//package com.fontana.log.producer.producer.internals;
//
//import org.junit.Test;
//
//public class ProducerBatchHolderTest {
//
//    /** Should do nothing when producerBatch is null */
//    @Test
//    public void transferProducerBatchWhenProducerBatchIsNull() {
//        ProducerBatchHolder producerBatchHolder = new ProducerBatchHolder();
//        producerBatchHolder.transferProducerBatch(mock(ExpiredBatches.class));
//        assertNull(producerBatchHolder.producerBatch);
//    }
//
//    /** Should add the producerBatch to expiredBatches when producerBatch is not null */
//    @Test
//    public void transferProducerBatchWhenProducerBatchIsNotNullThenAddToExpiredBatches() {
//        ProducerBatchHolder producerBatchHolder = new ProducerBatchHolder();
//        ProducerBatch producerBatch = mock(ProducerBatch.class);
//        producerBatchHolder.producerBatch = producerBatch;
//        ExpiredBatches expiredBatches = mock(ExpiredBatches.class);
//
//        producerBatchHolder.transferProducerBatch(expiredBatches);
//
//        verify(expiredBatches).add(producerBatch);
//    }
//}