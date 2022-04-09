package engine.vv.controller;

import engine.vv.exceptions.BadRequestException;
import engine.vv.exceptions.ForbiddenException;
import engine.vv.exceptions.NotFoundException;
import engine.vv.model.*;
import engine.vv.service.QuizService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@Slf4j
public class QuizController {

    @Autowired
    private QuizService quizService;

//    @GetMapping("/api/quiz")
//    public VVQuiz getQuiz() {
//        return VVQuiz.builder().title("The Java Logo")
//                .text("What is depicted on the Java logo?")
//                .options(List.of(
//                        "Robot",
//                        "Tea leaf",
//                        "Cup of coffee",
//                        "Bug"
//                )).build();
//    }
//
//    @PostMapping(value={"/api/quiz"})
//    public VVQuizResponse submitQuiz(int answer) {
//        if(answer == 2) {
//            return VVQuizResponse.builder()
//                    .success(true)
//                    .feedback("Congratulations, you're right!").build();
//        }
//        return VVQuizResponse.builder()
//                .success(false)
//                .feedback("Wrong answer! Please, try again.").build();
//    }

    @PostMapping("/api/quizzes")
    public VVQuizCreationResponse createQuiz(@RequestBody VVQuiz quiz) {
        log.info("VV3 - QuizController.createQuiz()");
        return quizService.createQuiz(quiz);
    }

    @GetMapping("/api/quizzes/{id}")
    public VVQuizCreationResponse getQuiz(@PathVariable("id") int id) {
//        log.info("VV3 - QuizController.getQuiz()");
        return quizService.getQuiz(id);
    }

//    @GetMapping("/api/quizzes")
//    public List<VVQuizCreationResponse> getQuizzes() {
//        log.info("VV3 - QuizController.getQuizzes()");
//        return quizService.getQuizzes();
//    }

    @GetMapping("/api/quizzes")
    public Page<VVQuizCreationResponse> getPaginatedQuizzes(@Qualifier("page") int page) {
//        log.info("VV3 - QuizController.getPaginatedQuizzes()");
        return quizService.getPaginatedQuizzes(page);
    }

    @GetMapping("/api/quizzes/completed")
    public Page<VVQuizCompletion> getCompletedPaginatedQuizzes(@Qualifier("page") int page) {
//        log.info("VV3 - QuizController.getCompletedPaginatedQuizzes()");
        return quizService.getCompletedPaginatedQuizzes(page);
    }

    @PostMapping("/api/quizzes/{id}/solve")
    public VVQuizResponse submitQuiz(@PathVariable("id") int id, @RequestBody   VVAnswerRequest answer) {
        log.info("VV3 - QuizController.submitQuiz()");
        return quizService.submitQuiz(id, answer.getAnswer());
    }

    @PostMapping(value = {"/api/register","/api/register/new"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public void createRegsitration(@RequestBody VVRegistration registration) {
//        log.info("<## VV ##> POST registration");
        checkEmail(registration);
        checkPassword(registration);
        if (quizService.alreadyExists(registration.getEmail())) {
            throw new BadRequestException();
        }
        VVRegistration result = quizService.saveRegistration(registration);
//        log.info("VV9 <## VV ##> POST registration result: " + result);
    }

    private void checkPassword(VVRegistration registration) {
        if (registration.getPassword() == null || registration.getPassword().isBlank()) {
//            log.info("VV9 <## VV ##> registration password is null");
            throw new BadRequestException();
        }
        if (registration.getPassword().length() < 5) {
//            log.info("VV9 <## VV ##> registration password is too short");
            throw new BadRequestException();
        }
    }

    private void checkEmail(VVRegistration registration) {
        if (registration.getEmail() == null || registration.getEmail().isBlank()) {
//            log.info("VV9 <## VV ##> registration email is null");
            throw new BadRequestException();
        }
        EmailValidator emailValidator = EmailValidator.getInstance();
        if (!emailValidator.isValid(registration.getEmail())) {
//            log.info("VV9 <## VV ##> registration email is invalid");
            throw new BadRequestException();
        }
    }

    @DeleteMapping("/api/quizzes/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteQuiz(@PathVariable("id") int vvQuizzId) {
        VVQuiz quiz = quizService.find(vvQuizzId);
        checkNotFound(quiz);
        checkIfUserIsOwner(quiz);
        log.info("VV9: delete quiz->"+vvQuizzId);
        quizService.delete(vvQuizzId);
    }

    private void checkNotFound(VVQuiz quiz) {
        if (quiz == null) {
//            log.info("VV9: quiz not found");
            throw new NotFoundException();
        }
    }

    private void checkIfUserIsOwner(VVQuiz recipe) {
//        log.info("VV153 owner [ctrlr.checkIfUserIsOwner]->"+recipe.getOwner());
//        log.info("VV153 recipe [ctrlr.checkIfUserIsOwner]->"+recipe);
        String currentUser= SecurityContextHolder.getContext().getAuthentication().getName();
            if (!currentUser.toLowerCase(Locale.ROOT).equals(recipe.getOwner().toLowerCase(Locale.ROOT))) {
            throw new ForbiddenException();
        }
    }

}
