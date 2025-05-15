package ru.job4j.bmb.services;

import org.jvnet.hk2.annotations.Service;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.model.MoodLog;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.repository.AchievementRepository;
import ru.job4j.bmb.repository.MoodLogRepository;
import ru.job4j.bmb.repository.MoodRepository;
import ru.job4j.bmb.repository.UserRepository;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class MoodService {
    private final MoodLogRepository moodLogRepository;
    private final RecommendationEngine recommendationEngine;
    private final UserRepository userRepository;
    private final AchievementRepository achievementRepository;
    private  final MoodRepository moodRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter
            .ofPattern("dd-MM-yyyy HH:mm")
            .withZone(ZoneId.systemDefault());

    static final long SECOND = 1000;
    static final long MINUTE = SECOND * 60;
    static final long HOUR = MINUTE * 60;
    static final long DAY = HOUR * 24;
    static final long MONTH = DAY * 30;

    public MoodService(MoodLogRepository moodLogRepository,
                       MoodRepository moodRepository,
                       RecommendationEngine recommendationEngine,
                       UserRepository userRepository,
                       AchievementRepository achievementRepository) {
        this.moodLogRepository = moodLogRepository;
        this.moodRepository = moodRepository;
        this.recommendationEngine = recommendationEngine;
        this.userRepository = userRepository;
        this.achievementRepository = achievementRepository;
    }

    public Content chooseMood(User user, Long moodId) {
        moodLogRepository.save(new MoodLog(user, moodRepository.findMoodById(moodId)));
        return recommendationEngine.recommendFor(user.getChatId(), moodId);
    }

    public Optional<Content> weekMoodLogCommand(long chatId, Long clientId) {
        var content = new Content(chatId);
        List<MoodLog> moodLogs = moodLogRepository
                .findAll()
                .stream()
                .filter(p -> p.getUser().getClientId() == (clientId)
                        && p.getUser().getChatId() == chatId
                        && p.getCreatedAt() > (System.currentTimeMillis()) - (DAY * 7))
                .toList();
        content.setText(formatMoodLogs(moodLogs, "Настроение пользователя за последние 7 дней."));
        return Optional.of(content);
    }

    public Optional<Content> monthMoodLogCommand(long chatId, Long clientId) {
        var content = new Content(chatId);
        List<MoodLog> moodLogs = moodLogRepository
                .findAll()
                .stream()
                .filter(p -> p.getUser().getClientId() == (clientId)
                        && p.getUser().getChatId() == chatId
                        && p.getCreatedAt() > (System.currentTimeMillis()) - MONTH)
                .toList();
        content.setText(formatMoodLogs(moodLogs, "Настроение пользователя за последние 30 дней."));
        return Optional.of(content);
    }

    private String formatMoodLogs(List<MoodLog> logs, String title) {
        if (logs.isEmpty()) {
            return title + ":\nNo mood logs found.";
        }
        var sb = new StringBuilder(title + ":\n");
        logs.forEach(log -> {
            String formattedDate = formatter.format(Instant.ofEpochSecond(log.getCreatedAt()));
            sb.append(formattedDate).append(": ").append(log.getMood().getText()).append("\n");
        });
        return sb.toString();
    }

    public Optional<Content> awards(long chatId, Long clientId) {
        var content = new Content(chatId);
        achievementRepository
                .findAll()
                .stream()
                .filter(p -> p.getUser().getClientId() == (clientId)
                        && p.getUser().getChatId() == chatId)
                .forEach(awards -> {
                    content.setText(awards.getAward().getTitle() + "\n");
                });
        return Optional.of(content);
    }
}

