package com.yicj.study.filter;

import com.yicj.study.model.entity.AuditLog;
import com.yicj.study.model.entity.User;
import com.yicj.study.repository.AuditLogRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class AuditLogInterceptor implements HandlerInterceptor {

    @Autowired
    private AuditLogRepository repository ;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AuditLog log = new AuditLog() ;
        log.setMethod(request.getMethod());
        log.setPath(request.getRequestURI());
        User user = (User)request.getSession().getAttribute("user");
        if (user != null){
            log.setUsername(user.getUsername());
        }
        Date now = DateTime.now().toDate();
        log.setCreatedTime(now);
        log.setModifyTime(now);
        repository.save(log) ;
        request.setAttribute("auditLogId", log.getId());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        Integer auditLogId = (Integer)request.getAttribute("auditLogId");
        AuditLog auditLog = repository.findById(auditLogId).get();
        auditLog.setStatus(response.getStatus());
        auditLog.setModifyTime(DateTime.now().toDate());
        repository.save(auditLog) ;
    }
}
