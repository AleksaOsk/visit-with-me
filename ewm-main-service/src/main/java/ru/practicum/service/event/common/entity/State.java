package ru.practicum.service.event.common.entity;

public enum State {
    PENDING, //в ожидании ревью
    PUBLISHED, //опубликовано
    CANCELED, //отменено до публикации
    SEND_TO_REVIEW, //отправлено на ревью пользователем
    CANCEL_REVIEW, //отмена отправки на ревью пользователем
    PUBLISH_EVENT, //согласование публикации админом
    REJECT_EVENT; //отклонение публикации админом

    @Override
    public String toString() {
        return super.toString();
    }

    public static State fromString(String state) {
        state = state.toUpperCase();
        return switch (state) {
            case "PENDING" -> State.PENDING;
            case "PUBLISHED" -> State.PUBLISHED;
            case "CANCELED" -> State.CANCELED;
            case "SEND_TO_REVIEW" -> State.SEND_TO_REVIEW;
            case "CANCEL_REVIEW" -> State.CANCEL_REVIEW;
            case "PUBLISH_EVENT" -> State.PUBLISH_EVENT;
            case "REJECT_EVENT" -> State.REJECT_EVENT;
            default -> null;
        };
    }
}
