

ALTER TABLE `op_upload_download`
  ADD COLUMN `exception_remark` TEXT NULL   COMMENT '导出失败的异常原因' AFTER `remark`;
