package com.agincourt.chessstat.repository;

import com.agincourt.chessstat.models.ChessGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChessGameRepository extends JpaRepository<ChessGame, Long> {

    List<ChessGame> findAllByOrderByLastMoveAtAsc();

    List<ChessGame> findByPlayerColorIsOrderByLastMoveAtAsc(String color);

    List<ChessGame> findByLastMoveAtAfterOrderByLastMoveAtAsc(Long timestamp);

    List<ChessGame> findByPlayerColorIsAndLastMoveAtOrderByLastMoveAtAsc(String color, Long timestamp);

}
