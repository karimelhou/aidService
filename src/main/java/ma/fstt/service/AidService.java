package ma.fstt.service;


import jakarta.transaction.Transactional;
import ma.fstt.common.exceptions.RecordNotFoundException;
import ma.fstt.common.messages.BaseResponse;
import ma.fstt.common.messages.CustomMessage;
import ma.fstt.common.utils.Topic;
import ma.fstt.dto.AidDTO;
import ma.fstt.dto.UserDTO;
import ma.fstt.entity.AidEntity;
import ma.fstt.entity.TypeAid;
import ma.fstt.repo.AidRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AidService {
    @Autowired
    private AidRepo aidRepo;


    private final WebClient webClient;

    public AidService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8082/api/v1/auth/users").build();
    }

    public List<AidDTO> findAidList() {
        return aidRepo.findAll().stream().map(this::copyAidEntityToDto).collect(Collectors.toList());
    }

    public List<AidDTO> findAidByUserId(Long id) {
        return aidRepo.findByUserId(id).stream().map(this::copyAidEntityToDto).collect(Collectors.toList());
    }

    public AidDTO findByAidId(Long aidId) {
        AidEntity userEntity = aidRepo.findById(aidId)
                .orElseThrow(() -> new RecordNotFoundException("Aid id '" + aidId + "' does not exist !"));
        return copyAidEntityToDto(userEntity);
    }

    public BaseResponse createOrUpdateAid(AidDTO aidDTO) {
        // Vérifie d'abord l'existence de l'utilisateur avant de créer une aid
        Mono<UserDTO> userMono = webClient.get()
                .uri("/{id}", aidDTO.getUserId())
                .retrieve()
                .bodyToMono(UserDTO.class);

        // Attendez la réponse du service d'authentification
        UserDTO userResponse = userMono.block();

        if (userResponse != null && userResponse.getId() != null) {
            // L'utilisateur existe, continuez avec la création ou la mise à jour de l'aid
            AidEntity aidEntity = copyAidDtoToEntity(aidDTO);
            aidRepo.save(aidEntity);
            return new BaseResponse(Topic.ASSISTANCE.getName() + CustomMessage.SAVE_SUCCESS_MESSAGE, HttpStatus.CREATED.value());
        } else {
            // L'utilisateur n'existe pas, vous pouvez gérer cela en lançant une exception, par exemple
            throw new RecordNotFoundException("L'utilisateur avec l'ID " + aidDTO.getUserId() + " n'existe pas.");
        }

    }

    public BaseResponse updateAid(Long aidId, AidDTO updatedAidDTO) {
        // Check if the aid with the given ID exists in the database
        if (!aidRepo.existsById(aidId)) {
            throw new RecordNotFoundException("Aid id '" + aidId + "' does not exist!");
        }

        // Find the existing AidEntity by ID
        AidEntity existingAidEntity = aidRepo.findById(aidId)
                .orElseThrow(() -> new RecordNotFoundException("Aid id '" + aidId + "' does not exist !"));

        // Update the fields of the existing entity with the values from the updated DTO
        existingAidEntity.setType(updatedAidDTO.getType());
        existingAidEntity.setAmount(updatedAidDTO.getAmount());
        existingAidEntity.setDetails(updatedAidDTO.getDetails());


        // Save the updated entity back to the database
        aidRepo.save(existingAidEntity);
        return new BaseResponse(Topic.ASSISTANCE.getName() + CustomMessage.UPDATE_SUCCESS_MESSAGE, HttpStatus.OK.value());
    }

    public BaseResponse deleteAid(Long aidId) {
        if (aidRepo.existsById(aidId)) {
            aidRepo.deleteById(aidId);
        } else {
            throw new RecordNotFoundException("No record found for given id: " + aidId);
        }
        return new BaseResponse(Topic.ASSISTANCE.getName() + CustomMessage.DELETE_SUCCESS_MESSAGE, HttpStatus.OK.value());
    }


    private AidDTO copyAidEntityToDto(AidEntity aidEntity) {
        AidDTO aidDTO = new AidDTO();
        aidDTO.setAidId(aidEntity.getAidId());
        aidDTO.setAmount(aidEntity.getAmount());
        aidDTO.setType(aidEntity.getType());
        aidDTO.setDetails(aidEntity.getDetails());
        aidDTO.setUserId(aidEntity.getUserId());
        return aidDTO;
    }

    private AidEntity copyAidDtoToEntity(AidDTO aidDTO) {
        AidEntity userEntity = new AidEntity();
        userEntity.setAidId(aidDTO.getAidId());
        userEntity.setDetails(aidDTO.getDetails());
        userEntity.setAmount(aidDTO.getAmount());
        userEntity.setType(aidDTO.getType());
        userEntity.setUserId(aidDTO.getUserId());
        return userEntity;
    }

    public List<AidDTO> findByType(TypeAid type) {
        return aidRepo.findByType(type).stream().map(this::copyAidEntityToDto).collect(Collectors.toList());
    }
}
