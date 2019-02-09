package com.jerome.demographic.person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Transactional(readOnly = true)
    public List<PersonDto> findAll() {
        return personRepository.findAll()
                .stream()
                .map(person -> PersonDto.builder()
                        .id(person.getId())
                        .name(person.getName())
                        .ppsn(person.getPpsn())
                        .dateOfBirth(person.getDateOfBirth())
                        .mobilePhone(person.getMobilePhone())
                        .build()
                ).collect(Collectors.toList());
    }

    @Transactional
    public void addNewPerson(PersonRequest personRequest) {
        checkIfThePersonWasAlreadyAdded(personRequest);
        personRepository.save(
                mapPersonRequestToPerson(personRequest)
        );
    }

    private void checkIfThePersonWasAlreadyAdded(PersonRequest personRequest) {
        if (personRepository.findByPpsn(personRequest.getPpsn()).isPresent()) {
            throw new PersonWithPpsnAlreadyExistException(personRequest.getPpsn());
        }
    }

    private Person mapPersonRequestToPerson(PersonRequest personRequest) {
        return Person.builder()
                .name(personRequest.getName())
                .ppsn(personRequest.getPpsn())
                .dateOfBirth(personRequest.getDateOfBirth())
                .mobilePhone(personRequest.getMobilePhone())
                .build();
    }
}
