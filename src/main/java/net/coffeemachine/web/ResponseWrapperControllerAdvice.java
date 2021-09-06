package net.coffeemachine.web;

import org.springframework.core.MethodParameter;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import net.coffeemachine.util.WrappedResponse;
import net.coffeemachine.web.controller.CoffeeMachineController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@ControllerAdvice(annotations = WrappedResponse.class)
public class ResponseWrapperControllerAdvice implements ResponseBodyAdvice {
    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        return EntityModel.of(
                o,
                linkTo(methodOn(CoffeeMachineController.class).start()).withRel("Start"),
                linkTo(methodOn(CoffeeMachineController.class).stop()).withRel("Stop"),
                linkTo(methodOn(CoffeeMachineController.class).remains()).withRel("Remains"),
                linkTo(methodOn(CoffeeMachineController.class).clean()).withRel("Clean"),
                linkTo(methodOn(CoffeeMachineController.class).make(null)).withRel("Make")
        );
    }
}
