package org.example.dtos;

public record LoginResponse(String token,
                            long expiresIn) { }
