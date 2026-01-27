package com.newland.ucms.certificate.controller;

import com.newland.ucms.certificate.dto.FieldLibraryDTO;
import com.newland.ucms.certificate.service.FieldLibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 字段库控制器
 *
 * @author UCMS Team
 * @since 2026-01-27
 */
@RestController
@RequestMapping("/api/field")
public class FieldLibraryController {

  @Autowired
  private FieldLibraryService fieldLibraryService;

  /**
   * 获取公共字段列表
   *
   * @return 公共字段列表
   */
  @GetMapping("/public")
  public ResponseEntity<List<FieldLibraryDTO>> getPublicFields() {
    List<FieldLibraryDTO> fields = fieldLibraryService.getPublicFields();
    return ResponseEntity.ok(fields);
  }

  /**
   * 根据字段类型查询字段列表
   *
   * @param fieldType 字段类型（PUBLIC/CUSTOM）
   * @return 字段列表
   */
  @GetMapping("/type/{fieldType}")
  public ResponseEntity<List<FieldLibraryDTO>> getFieldsByType(@PathVariable String fieldType) {
    List<FieldLibraryDTO> fields = fieldLibraryService.getFieldsByType(fieldType);
    return ResponseEntity.ok(fields);
  }

  /**
   * 创建自定义字段
   *
   * @param fieldLibraryDTO 字段信息
   * @return 创建后的字段信息
   */
  @PostMapping("/custom")
  public ResponseEntity<FieldLibraryDTO> createCustomField(@RequestBody FieldLibraryDTO fieldLibraryDTO) {
    FieldLibraryDTO field = fieldLibraryService.createCustomField(fieldLibraryDTO);
    return ResponseEntity.ok(field);
  }

  /**
   * 更新字段
   *
   * @param id 字段ID
   * @param fieldLibraryDTO 字段信息
   * @return 更新后的字段信息
   */
  @PutMapping("/{id}")
  public ResponseEntity<FieldLibraryDTO> updateField(
      @PathVariable Long id,
      @RequestBody FieldLibraryDTO fieldLibraryDTO) {
    FieldLibraryDTO field = fieldLibraryService.updateField(id, fieldLibraryDTO);
    return ResponseEntity.ok(field);
  }

  /**
   * 删除字段
   *
   * @param id 字段ID
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteField(@PathVariable Long id) {
    fieldLibraryService.deleteField(id);
    return ResponseEntity.ok().build();
  }
}
