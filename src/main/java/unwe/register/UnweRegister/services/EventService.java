package unwe.register.UnweRegister.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import unwe.register.UnweRegister.dtos.events.EventCatalogResponse;
import unwe.register.UnweRegister.dtos.events.EventResponse;
import unwe.register.UnweRegister.entities.ActivityPlan;
import unwe.register.UnweRegister.entities.Event;
import unwe.register.UnweRegister.exceptions.ElementNotPresentException;
import unwe.register.UnweRegister.exceptions.FieldMissingException;
import unwe.register.UnweRegister.exceptions.FieldValidationException;
import unwe.register.UnweRegister.exceptions.InvalidOperationException;
import unwe.register.UnweRegister.repositories.EventRepository;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    private static final String DESCRIPTION_TOO_LONG = "Description must not be more than 2000 symbols!";
    private static final String YOU_CANNOT_CREATE_EVENTS_FOR_THIS_ACTIVITY_PLAN = "You cannot create events for this activity plan";
    private static final String EVENT_NOT_FOUND = "Event not found!";
    private static final int EVENTS_PER_PAGE = 10;
    private static final String TITLE_MUST_NOT_BE_EMPTY = "Title must not be empty!";
    private static final String DESCRIPTION_MUST_NOT_BE_EMPTY = "Description must not be empty!";
    private static final int DESCRIPTION_MAX_SYMBOLS = 2000;
    private static final String TITLE_TOO_LONG = "Title must not be more than 200 symbols!";
    private static final int TITLE_MAX_SYMBOLS = 250;
    private static final String YOU_CANNOT_DELETE_THIS_EVENT = "You cannot delete this event!";
    private static final String SUCCESS_DELETE = "You successfully deleted the event!";
    private static final String YOU_CANNOT_EDIT_THIS_EVENT = "You cannot edit this event!";

    private final EventRepository eventRepository;

    private final ModelMapper modelMapper;

    private final ActivityPlanService activityPlanService;

    @Value("${picture.url.event}")
    private String eventImageUrl;

    @Autowired
    public EventService(EventRepository eventRepository, ModelMapper modelMapper, ActivityPlanService activityPlanService) {
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
        this.activityPlanService = activityPlanService;
    }

    public EventResponse addEvent(String title, String description, String activityPlanId,
                                  MultipartFile multipartFile, String userId) throws IOException {

        ActivityPlan activityPlan = activityPlanService.getActivityPlan(activityPlanId);

        validateAddEventData(title, description, userId, activityPlan);

        Event event = new Event(activityPlan, title, description);
        if (multipartFile != null) {
            event.setImage(multipartFile.getBytes());
        }

        EventResponse eventResponse = modelMapper.map(eventRepository.save(event), EventResponse.class);
        eventResponse.setImageUrl(eventImageUrl + event.getUid());

        return eventResponse;
    }

    private void validateAddEventData(String title, String description, String userId, ActivityPlan activityPlan) {
        if (title.isBlank()) {
            throw new FieldMissingException(TITLE_MUST_NOT_BE_EMPTY);
        }

        if (description.isBlank()) {
            throw new FieldMissingException(DESCRIPTION_MUST_NOT_BE_EMPTY);
        }

        if (description.length() > DESCRIPTION_MAX_SYMBOLS) {
            throw new FieldValidationException(DESCRIPTION_TOO_LONG);
        }

        if (title.length() > TITLE_MAX_SYMBOLS) {
            throw new FieldValidationException(TITLE_TOO_LONG);
        }

        if (!activityPlan.getAgreement().getEmployer().getUid().equals(userId)) {
            throw new InvalidOperationException(YOU_CANNOT_CREATE_EVENTS_FOR_THIS_ACTIVITY_PLAN);
        }
    }

    private Event getEvent(String eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new ElementNotPresentException(EVENT_NOT_FOUND));
    }

    public byte[] getEventPicture(String eventId) {
        return getEvent(eventId).getImage();
    }

    public EventCatalogResponse getEvents(int page, String search) {
        List<EventResponse> events = eventRepository.findAllByTitleContainingOrderByMadeOnDesc(PageRequest.of(page, EVENTS_PER_PAGE), search)
                .stream()
                .map(event -> {
                    EventResponse eventResponse = modelMapper.map(event, EventResponse.class);
                    if (event.getImage() != null) {
                        eventResponse.setImageUrl(eventImageUrl + event.getUid());
                    }
                    return eventResponse;
                })
                .collect(Collectors.toList());

        return new EventCatalogResponse(events, eventRepository.countByTitleContaining(search));
    }

    public String deleteEvent(String eventId, String userId) {
        Event event = getEvent(eventId);

        if (!event.getActivityPlan().getAgreement().getEmployer().getUid().equals(userId)) {
            throw new InvalidOperationException(YOU_CANNOT_DELETE_THIS_EVENT);
        }
        eventRepository.delete(event);
        return SUCCESS_DELETE;
    }

    public EventResponse editEvent(String title, String description, String activityPlanId, String eventId,
                                   MultipartFile multipartFile, String userId) throws IOException {
        Event event = getEvent(eventId);

        if (!event.getActivityPlan().getAgreement().getEmployer().getUid().equals(userId)) {
            throw new InvalidOperationException(YOU_CANNOT_EDIT_THIS_EVENT);
        }

        ActivityPlan activityPlan = activityPlanService.getActivityPlan(activityPlanId);

        validateAddEventData(title, description, userId, activityPlan);

        event.setActivityPlan(activityPlan);
        event.setTitle(title);
        event.setDescription(description);

        if (multipartFile != null) {
            event.setImage(multipartFile.getBytes());
        }

        EventResponse eventResponse = modelMapper.map(eventRepository.save(event), EventResponse.class);
        eventResponse.setImageUrl(eventImageUrl + event.getUid());

        return eventResponse;
    }

    public EventResponse getEventById(String eventId) {
        return modelMapper.map(getEvent(eventId), EventResponse.class);
    }
}
