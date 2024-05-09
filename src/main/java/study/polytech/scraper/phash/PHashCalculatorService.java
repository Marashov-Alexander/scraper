package study.polytech.scraper.phash;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
public class PHashCalculatorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PHashCalculatorService.class);

    private final PHashCalculator pHashCalculator;

    public PHashCalculatorService(@Value("${path.library.phash}") String libraryPath) {
        System.load(libraryPath);
        pHashCalculator = new PHashCalculator();
    }

    @Nullable
    public Long calculateHash(Path imageDirectoryPath) {
        long startTime = System.currentTimeMillis();
        try {
            long hash = pHashCalculator.calculateHashes(imageDirectoryPath.toString() + "|")[0];
            long deltaTime = System.currentTimeMillis() - startTime;
            LOGGER.info("For path [{}] got hash [{}] for [{}] ms", imageDirectoryPath, hash, deltaTime);
            return hash;
        } catch (Throwable e) {
            long deltaTime = System.currentTimeMillis() - startTime;
            LOGGER.error("Hash calculation error for path [{}], took [{}] ms", imageDirectoryPath, deltaTime, e);
            return null;
        }
    }
}
