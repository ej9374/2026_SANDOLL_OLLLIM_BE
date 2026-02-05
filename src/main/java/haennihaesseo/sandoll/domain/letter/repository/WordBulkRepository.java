package haennihaesseo.sandoll.domain.letter.repository;

import haennihaesseo.sandoll.domain.letter.entity.Word;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class WordBulkRepository {

    private final JdbcTemplate jdbcTemplate;

    public void batchInsert(List<Word> words) {
        String sql = "INSERT INTO words (word, start_time, end_time, letter_id, created_at) " +
                "VALUES (?, ?, ?, ?, NOW())";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Word word = words.get(i);
                ps.setString(1, word.getWord());
                ps.setObject(2, word.getStartTime());
                ps.setObject(3, word.getEndTime());
                ps.setLong(4, word.getLetter().getLetterId());
            }

            @Override
            public int getBatchSize() {
                return words.size();
            }
        });
    }
}
