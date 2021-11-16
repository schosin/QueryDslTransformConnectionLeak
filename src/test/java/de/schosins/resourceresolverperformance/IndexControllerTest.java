package de.schosins.resourceresolverperformance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.metadata.HikariDataSourcePoolMetadata;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import com.zaxxer.hikari.HikariDataSource;

import de.schosins.resourceresolverperformance.database.TestEntity;
import de.schosins.resourceresolverperformance.database.TestRepo;

@SpringBootTest
@AutoConfigureMockMvc
class IndexControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    TestRepo repo;

    @Autowired
    TransactionTemplate txTemplate;

    @Autowired
    HikariDataSource dataSource;

    @BeforeEach
    @Transactional
    public void deleteEntities() {
        repo.deleteAll();
    }

    @ParameterizedTest
    @ValueSource(strings = { "/transactional", "/txtemplate", "/notransaction" })
    public void test(String path) throws Exception {
        var activeConnections = getActiveConnections();

        createEntities(2);
        assertThat(getActiveConnections()).as("active connections unchanged").isEqualTo(activeConnections);

        mockMvc.perform(get(path))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.aMapWithSize(2)));

        assertThat(getActiveConnections()).as("active connections unchanged").isEqualTo(activeConnections);
    }

    private int getActiveConnections() {
        return new HikariDataSourcePoolMetadata(dataSource).getActive();
    }

    private void createEntities(int count) {
        txTemplate.executeWithoutResult(txStatus -> {
            for (int i = 0; i < count; i++) {
                repo.save(new TestEntity("name" + i));
            }
        });
    }

}
