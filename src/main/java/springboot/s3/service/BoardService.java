package springboot.s3.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.s3.domain.Board;
import springboot.s3.domain.BoardRepository;
import springboot.s3.dto.BoardDTO;
import springboot.s3.file.FileStore;

import java.io.IOException;
import java.util.Map;

@Transactional
@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final FileStore fileStore;

    /**
     * 게시물 저장
     */
    @Transactional
    public Board save(BoardDTO boardDTO) throws IOException {

        Map<String, String> pathMap = fileStore.storeFile(boardDTO.getImageFile());

        Board board = boardDTO.toEntity(pathMap);

        boardRepository.save(board);

        return board;
    }

}
