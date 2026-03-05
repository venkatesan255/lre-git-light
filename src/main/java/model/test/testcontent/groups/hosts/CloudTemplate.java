package model.test.testcontent.groups.hosts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CloudTemplate {


    @JsonProperty("Id")
    private Integer id;

    @JsonProperty("AccountId")
    private Integer accountId;

    @JsonProperty("AccountName")
    private String accountName;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Image")
    private String image;

    @JsonProperty("InstanceType")
    private String instanceType;

    @JsonProperty("KeyPairName")
    private String keyPairName;

    @JsonProperty("SecurityGroupName")
    private String securityGroupName;

    @JsonProperty("Description")
    private String description;

    @JsonProperty("Installation")
    private String installation;

    @JsonProperty("VpcSubnetId")
    private String vpcSubnetId;

    @JsonProperty("Platform")
    private String platform;

    @JsonProperty("IsValid")
    private Boolean isValid;

    @JsonProperty("NetworkName")
    private String networkName;

    @JsonProperty("Region")
    private String region;

    @JsonProperty("UseElasticIp")
    private Boolean useElasticIp;

    @JsonProperty("UsePrivateIp")
    private Boolean usePrivateIp;
}
