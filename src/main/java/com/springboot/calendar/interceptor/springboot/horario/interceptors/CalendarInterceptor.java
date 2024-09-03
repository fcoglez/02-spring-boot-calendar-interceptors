package com.springboot.calendar.interceptor.springboot.horario.interceptors;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class CalendarInterceptor implements HandlerInterceptor {

    @Value("${config.calendar.open}")
    private Integer open;
    @Value("${config.calendar.close}")
    private Integer close;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        System.out.println("Son las " + hour);

        if (hour >= open && hour <= close) {
            StringBuilder message = new StringBuilder("Bienvenidos al horario de atención a clientes");
            message.append(", atendemos desde las ");
            message.append(open);
            message.append(" horas");
            message.append(" hasta las ");
            message.append(close);
            message.append(" horas");
            message.append(" Gracias por su visita.");
            request.setAttribute("message", message.toString());
            return true;
        }
        // Crea un mapa JSON con un mensaje de error y la fecha actual, lo convierte a una cadena JSON,
        Map<String, String> json = new HashMap<>();
        json.put("error", "Estas fuera del horario establecido, abrimos desde las " + open + " horas, hasta las " + close + " horas.");
        json.put("date", new Date().toString());

        // Establece la respuesta HTTP como JSON con un estado 401 (No autorizado) y escribe la cadena JSON en la respuesta.
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(json);
        response.setContentType("application/json");
        response.setStatus(401);
        response.getWriter().write(jsonString);
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }
}
