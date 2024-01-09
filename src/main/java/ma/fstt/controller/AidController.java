package ma.fstt.controller;


import ma.fstt.common.messages.BaseResponse;
import ma.fstt.dto.AidDTO;
import ma.fstt.entity.TypeAid;
import ma.fstt.service.AidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@Validated
@RestController
@RequestMapping("/aids")
public class AidController {

    @Autowired
    private AidService aidService;

    @GetMapping
    public ResponseEntity<List<AidDTO>> getAllAid() {
        List<AidDTO> list = aidService.findAidList();
        return new ResponseEntity<List<AidDTO>>(list, HttpStatus.OK);
    }

    @PostMapping(value = { "/add" })
    public ResponseEntity<BaseResponse> createAid(@RequestBody AidDTO userDTO) {
        BaseResponse response = aidService.createOrUpdateAid(userDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/requests/{type}")
    public ResponseEntity<List<AidDTO>> getAllAidByType(@PathVariable TypeAid type) {
        List<AidDTO> list = aidService.findByType(type);
        return new ResponseEntity<List<AidDTO>>(list, HttpStatus.OK);
    }

    @GetMapping(value = "/user/{id}")
    public ResponseEntity<List<AidDTO>> getAllAidByUserId(@PathVariable Long id) {
        List<AidDTO> list = aidService.findAidByUserId(id);
        return new ResponseEntity<List<AidDTO>>(list, HttpStatus.OK);
    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<BaseResponse> updateAid(
            @PathVariable("id") Long id,
            @RequestBody AidDTO updatedAidDTO) {

        BaseResponse response = aidService.updateAid(id, updatedAidDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<BaseResponse> deleteAidById(@PathVariable Long id) {
        BaseResponse response = aidService.deleteAid(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<AidDTO> getAidById(@PathVariable Long id) {
        AidDTO list = aidService.findByAidId(id);
        return new ResponseEntity<AidDTO>(list, HttpStatus.OK);
    }
}
