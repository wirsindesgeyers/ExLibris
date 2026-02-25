package com.biblioteca_api.biblioteca.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biblioteca_api.biblioteca.dto.ReviewResponseDTO;
import com.biblioteca_api.biblioteca.service.ReviewService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final ReviewService reviewService;

    @Operation(summary = "Retorna todas as reviews de um usuário")
    @GetMapping("/{userId}")
    public ResponseEntity<List<ReviewResponseDTO>> listAllReviews(@PathVariable Long userId) {
        List<ReviewResponseDTO> responseDTO = reviewService.listReviewsFromUser(userId);

        return ResponseEntity.ok(responseDTO);
    }

}
