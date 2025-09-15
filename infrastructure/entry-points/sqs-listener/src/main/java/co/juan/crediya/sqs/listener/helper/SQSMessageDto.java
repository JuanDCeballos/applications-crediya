package co.juan.crediya.sqs.listener.helper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SQSMessageDto {
    private Long applicationId;
    private String status;
}
