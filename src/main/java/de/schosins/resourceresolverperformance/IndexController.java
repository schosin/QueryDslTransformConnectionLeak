package de.schosins.resourceresolverperformance;

import java.util.Map;

import javax.persistence.EntityManager;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.GetMapping;

import com.querydsl.core.group.GroupBy;
import com.querydsl.jpa.impl.JPAQueryFactory;

import de.schosins.resourceresolverperformance.database.QTestEntity;

@Controller
public class IndexController {

    private static final QTestEntity TEST = QTestEntity.testEntity;

    private final JPAQueryFactory factory;
    private final TransactionTemplate txTemplate;

    public IndexController(EntityManager entityManager, PlatformTransactionManager transactionManager) {
        this.factory = new JPAQueryFactory(entityManager);
        this.txTemplate = new TransactionTemplate(transactionManager);
    }

    @Transactional(readOnly = true)
    @GetMapping("/transactional")
    public ResponseEntity<Map<Long, String>> transactional() {
        return fetchEntities();
    }

    @GetMapping("/txtemplate")
    public ResponseEntity<Map<Long, String>> txtemplate() {
        return txTemplate.execute(txStatus -> fetchEntities());
    }

    @GetMapping("/notransaction")
    public ResponseEntity<Map<Long, String>> notransaction() {
        return fetchEntities();
    }

    private ResponseEntity<Map<Long, String>> fetchEntities() {
        Map<Long, String> entities = factory.from(TEST).transform(GroupBy.groupBy(TEST.id).as(TEST.name));
        return entities.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(entities);
    }

}
