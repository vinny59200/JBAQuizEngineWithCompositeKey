package engine.vv.service;

import engine.vv.exceptions.BadRequestException;
import engine.vv.exceptions.NotFoundException;
import engine.vv.model.*;
import engine.vv.repository.DatedQuizRepository;
import engine.vv.repository.PaginatedQuizRepository;
import engine.vv.repository.RegistrationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class QuizService implements UserDetailsService {

//    @Autowired
//    private QuizRepository quizRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private PaginatedQuizRepository paginatedQuizRepository;

    @Autowired
    private DatedQuizRepository dateKeyQuizRepository;

    public VVQuizCreationResponse createQuiz(VVQuiz quiz) {
//        if (quiz.getKey().getDate() == null) {
        VVQuizId key = new VVQuizId();
        key.setQuizDate(LocalDateTime.now());
        if (quiz.getId() == null || (quiz.getId() != null && quiz.getId().getQuizId() == null)) {
            key.setQuizId(generateNewIdCuzFuckingAutoIncrementFoulsUp());
        }
        quiz = quiz.toBuilder().id(key).build();
//        }
//        log.info("VV3 - Creating quiz: " + quiz);
        VVQuizCreationResponse response = VVQuizCreationResponse.mapFromVVQuiz(quiz);
        try {
            String owner = SecurityContextHolder.getContext().getAuthentication().getName();
            quiz = quiz.toBuilder().owner(owner).build();
            log.info("VV3 - Creating quiz with id: " + quiz.getId() + " and result: " + quiz);
            VVQuiz result = dateKeyQuizRepository.save(quiz);
            return response.mapFromVVQuiz(result);
        } catch (TransactionSystemException e) {
            throw new BadRequestException();
        } catch (Exception e) {
            log.error("Error while creating quiz", e);
        }
        return response;
    }

    private Integer generateNewIdCuzFuckingAutoIncrementFoulsUp() {
        Integer newId = 0;
        List<VVQuiz> vvQuizzes = new ArrayList<>();
        dateKeyQuizRepository.findAll().forEach(vvQuizzes::add);
        for (VVQuiz VVQuiz : vvQuizzes) {
            if (VVQuiz.getId().getQuizId() > newId) {
                newId = VVQuiz.getId().getQuizId();
            }
        }
        newId++;
//        log.info("VV3 - Generated new id: " + newId);
        return newId;
    }

    public VVQuizCreationResponse getQuiz(Integer id) {
//        log.info("VV3 - Getting quiz with id: " + id);
        List<VVQuiz> result = dateKeyQuizRepository.findByIdQuizId(id);
        if (result == null || result.isEmpty()) {
            throw new NotFoundException();
        }
//        if(result.getAnswer().contains(null)) {
//            result.setAnswer(Collections.emptyList());
//        }
//        log.info("VV3 - Got quiz with id: " + id + " and result: " + result);
        return VVQuizCreationResponse.mapFromVVQuiz(result.get(0));
    }

    public List<VVQuizCreationResponse> getQuizzes() {
//        log.info("VV3 - Getting all quizzes");
        List<VVQuizCreationResponse> result = new ArrayList<>();
        dateKeyQuizRepository.findAll().forEach(quiz -> result.add(VVQuizCreationResponse.mapFromVVQuiz(quiz)));
        return result;
    }

    public VVQuizResponse submitQuiz(Integer id, List<Integer> answer) {
//        log.info("VV3 - Submitting quiz with id: " + id);
        List<VVQuiz> result = dateKeyQuizRepository.findByIdQuizId(id);
        if (result == null || result.isEmpty()) {
            throw new NotFoundException();
        }
        VVQuiz quiz = result.get(0);
        VVQuizId quizId = quiz.getId().toBuilder().quizDate(LocalDateTime.now()).build();
        String player = SecurityContextHolder.getContext().getAuthentication().getName();
//        dateKeyQuizRepository.save(quiz.toBuilder().id(quizId).build());
        if (isGoodAnswer(quiz.getAnswer(), answer)) {
            log.info("VV3 - solved quiz with id: " + id + " and result: " + result);
            dateKeyQuizRepository.save(quiz.toBuilder().id(quizId).solved(true).player(player).build());
            return VVQuizResponse.builder().success(true).feedback("Congratulations, you're right!").build();
        } else {
            log.info("VV3 - failed quiz with id: " + id + " and result: " + result);
            return VVQuizResponse.builder().success(false).feedback("Wrong answer! Please, try again.").build();
        }
    }

    private boolean isGoodAnswer(List<Integer> one, List<Integer> two) {
        log.info("VV3 - comparing  true answers: " + one + " with user answers " + two);
        if (one == null && two == null) {
            return true;
        }

        if ((one == null && two != null)
                || one != null && two == null
                || one.size() != two.size()) {
            return false;
        }

        //to avoid messing the order of the lists we will use a copy
        //as noted in comments by A. R. S.
        one = new ArrayList<Integer>(one);
        two = new ArrayList<Integer>(two);

        Collections.sort(one);
        Collections.sort(two);
        return one.equals(two);
    }

    public boolean alreadyExists(String email) {
//        log.info("<## VV ##> fetching registration by email: " + email);
        List<VVRegistration> result = registrationRepository.findByEmail(email);
        if (result.isEmpty()) {
            return false;
        }
        return true;
    }

    public VVRegistration saveRegistration(VVRegistration vvRegistration) {
//        log.info("<## VV ##> saving vvRegistration: " + vvRegistration);
        return registrationRepository.save(vvRegistration.toBuilder().role("ROLE_USER").id(generateNewIdCuzFuckingAutoIncrementFoulsUpRegistration()).build());
    }

    private Integer generateNewIdCuzFuckingAutoIncrementFoulsUpRegistration() {
        Integer newId = 0;
        List<VVRegistration> vvRegistrations = new ArrayList<>();
        registrationRepository.findAll().forEach(vvRegistrations::add);
        for (VVRegistration vvRegistration : vvRegistrations) {
            if (vvRegistration.getId() > newId) {
                newId = vvRegistration.getId();
            }
        }
        newId++;
        return newId;
    }

    public void delete(Integer id) {
//        log.info("<## VV ##> deleting quiz: " + id);
        dateKeyQuizRepository.deleteByIdQuizId(id);
    }

    public VVQuiz find(Integer id) {
        List<VVQuiz> result = dateKeyQuizRepository.findByIdQuizId(id);
        if (result == null || result.isEmpty()) {
            throw new NotFoundException();
        }
//        log.info("<## VV ##> fetching quiz" + id + ": " + result);
//        log.info("VV153 owner [service.find]: " + (result == null ? "null" : result.get(0).getOwner()));
        return result.get(0);
    }

    public Page<VVQuizCreationResponse> getPaginatedQuizzes(Integer page) {
//        log.info("<## VV ##> fetching quizzes paginated: " + page + " size: " + 10);
        Page<VVQuiz> all = paginatedQuizRepository.findAllNotSolved(PageRequest.of(page, 10));
        Page<VVQuizCreationResponse> retroFittedAll = all.map(VVQuizCreationResponse::mapFromVVQuiz);
        return retroFittedAll;
    }

    public Page<VVQuizCompletion> getCompletedPaginatedQuizzes(Integer page) {
//        log.info("<## VV ##> fetching completed quizzes paginated: ");
        String owner = SecurityContextHolder.getContext().getAuthentication().getName();
        Page<VVQuiz> all = paginatedQuizRepository.findByName(owner, PageRequest.of(page, 10));
        Page<VVQuizCompletion> retroFittedAll = all.map(VVQuizCompletion::mapFromVVQuiz);
        return retroFittedAll;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<VVRegistration> result = registrationRepository.findByEmail(username);
        if (result == null || result.isEmpty()) {
            //log.info("VV9: User not found->" + username);
            throw new UsernameNotFoundException("User not found");
        }
        UserDetails userDetails = new UserDetailsImpl(result.get(0));
        //log.info("VV9: User found->" + userDetails);
        return userDetails;
    }


    class UserDetailsImpl implements UserDetails {
        private final String username;
        private final String password;
        private final List<GrantedAuthority> rolesAndAuthorities;

        public UserDetailsImpl(VVRegistration user) {
            username = user.getEmail().toLowerCase(Locale.ROOT);
            password = user.getPassword();
            rolesAndAuthorities = List.of(new SimpleGrantedAuthority(user.getRole()));
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return rolesAndAuthorities;
        }

        @Override
        public String getPassword() {
            return password;
        }

        @Override
        public String getUsername() {
            return username;
        }

        // 4 remaining methods that just return true
        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }


    }
}
