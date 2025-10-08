//package com.ucm.Api_NotificacaoUCM.connections;
//
//import org.springframework.amqp.core.Binding;
//import org.springframework.amqp.core.DirectExchange;
//import org.springframework.amqp.core.Queue;
//import org.springframework.stereotype.Component;
//
//@Component
//public class RabbitMQConnection {
//    private static final String NOME_EXCHANGE  = "amq.direct";
//    private Queue fila(String nomeFila){
//        return new Queue(nomeFila, true, false, false);
//    }
//
//    private DirectExchange trocaDireta(){
//        return new DirectExchange(NOME_EXCHANGE);
//    }
//
//    private Binding relacionamento(Queue fila, DirectExchange troca){
//        return new Binding(fila.getName(), Binding.DestinationType.EXCHANGE, troca.getName(), fila.getName(), null);
//    }
//
//    private void adicionar(String nomeFila){
//        Queue filaNotificacao = this.fila(RabbitMQConstantes.FILA_NOTICACAO);
//        Queue filaTarefa = this.fila(RabbitMQConstantes.FILA_TAREFA);
//        Queue filaAnuncio = this.fila(RabbitMQConstantes.FILA_ANUNCIOS);
//
//        DirectExchange troca = this.trocaDireta();
//
//
//    }
//}
