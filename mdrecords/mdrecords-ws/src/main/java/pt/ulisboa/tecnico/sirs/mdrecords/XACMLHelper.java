package pt.ulisboa.tecnico.sirs.mdrecords;

import oasis.names.tc.xacml._3_0.core.schema.wd_17.DecisionType;
import org.ow2.authzforce.core.pdp.api.*;
import org.ow2.authzforce.core.pdp.api.value.AttributeBag;
import org.ow2.authzforce.core.pdp.api.value.Bags;
import org.ow2.authzforce.core.pdp.api.value.StandardDatatypes;
import org.ow2.authzforce.core.pdp.api.value.StringValue;
import org.ow2.authzforce.core.pdp.impl.BasePdpEngine;
import org.ow2.authzforce.core.pdp.impl.PdpEngineConfiguration;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;


/**
 * A class for build requests. For testing purposes only!!
 */
public class XACMLHelper {

    private static PdpEngineConfiguration getPDPEngine() throws IOException{
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        URL caURL = loader.getResource("control");
        String path_ca = caURL.getPath() + "/pdp.xml";
        return PdpEngineConfiguration.getInstance(path_ca);

    }

    public static boolean checkPersonPermission(String personType, String operationType, String readWrite) throws IOException{

       PdpEngineConfiguration pdpEngineConf = getPDPEngine();

        /*
         * Create the PDP engine. You can reuse the same for all requests, so do it only once for all.
         */

        final BasePdpEngine pdp = new BasePdpEngine(pdpEngineConf);

        // Create the XACML request in native model
        final DecisionRequestBuilder<?> requestBuilder = pdp.newRequestBuilder(-1,
                -1);

        Optional<String> empty = Optional.empty();

        // Add subject ID attribute (access-subject category), no issuer, string value "john"
        final AttributeFqn subjectIdAttributeId = AttributeFqns.newInstance("subject-role",
                empty, "subject-role");
        final AttributeBag<?> subjectIdAttributeValues = Bags.singletonAttributeBag(StandardDatatypes.STRING,
                new StringValue(personType));
        requestBuilder.putNamedAttributeIfAbsent(subjectIdAttributeId, subjectIdAttributeValues);


        final AttributeFqn reportAttributeId = AttributeFqns.newInstance("report-id-category",
                empty, "report-id");
        final AttributeBag<?> reportAttributeValues = Bags.singletonAttributeBag(StandardDatatypes.STRING,
                new StringValue(operationType));
        requestBuilder.putNamedAttributeIfAbsent(reportAttributeId, reportAttributeValues);

        // Add action ID attribute (action category), no issuer, string value "GET"
        final AttributeFqn actionIdAttributeId = AttributeFqns.newInstance("action-category", empty,
                "action-id");
        final AttributeBag<?> actionIdAttributeValues = Bags.singletonAttributeBag(StandardDatatypes.STRING,
                new StringValue(readWrite));
        requestBuilder.putNamedAttributeIfAbsent(actionIdAttributeId, actionIdAttributeValues);


        final DecisionRequest request = requestBuilder.build(false);
        // Evaluate the request
        final DecisionResult result = pdp.evaluate(request);

        return result.getDecision() == DecisionType.PERMIT;

    }

    public static boolean checkPersonPermission(String personType, String operationType, String readWrite, String following) throws IOException {

        PdpEngineConfiguration pdpEngineConf = getPDPEngine();
        /*
         * Create the PDP engine. You can reuse the same for all requests, so do it only once for all.
         */
        final BasePdpEngine pdp = new BasePdpEngine(pdpEngineConf);

        // Create the XACML request in native model
        final DecisionRequestBuilder<?> requestBuilder = pdp.newRequestBuilder(-1,
                -1);

        Optional<String> empty = Optional.empty();

        // Add subject ID attribute (access-subject category), no issuer, string value "john"
        final AttributeFqn subjectIdAttributeId = AttributeFqns.newInstance("subject-role",
                empty, "subject-role");
        final AttributeBag<?> subjectIdAttributeValues = Bags.singletonAttributeBag(StandardDatatypes.STRING,
                new StringValue(personType));
        requestBuilder.putNamedAttributeIfAbsent(subjectIdAttributeId, subjectIdAttributeValues);


        final AttributeFqn reportAttributeId = AttributeFqns.newInstance("report-id-category",
                empty, "report-id");
        final AttributeBag<?> reportAttributeValues = Bags.singletonAttributeBag(StandardDatatypes.STRING,
                new StringValue(operationType));
        requestBuilder.putNamedAttributeIfAbsent(reportAttributeId, reportAttributeValues);

        final AttributeFqn followingstatus = AttributeFqns.newInstance("following-category",
                empty, "following-status");
        final AttributeBag<?> followingAttributeValues = Bags.singletonAttributeBag(StandardDatatypes.STRING,
                new StringValue(following));
        requestBuilder.putNamedAttributeIfAbsent(followingstatus, followingAttributeValues);

        // Add action ID attribute (action category), no issuer, string value "GET"
        final AttributeFqn actionIdAttributeId = AttributeFqns.newInstance("action-category", empty,
                "action-id");
        final AttributeBag<?> actionIdAttributeValues = Bags.singletonAttributeBag(StandardDatatypes.STRING,
                new StringValue(readWrite));
        requestBuilder.putNamedAttributeIfAbsent(actionIdAttributeId, actionIdAttributeValues);


        final DecisionRequest request = requestBuilder.build(false);
        // Evaluate the request
        final DecisionResult result = pdp.evaluate(request);

        return result.getDecision() == DecisionType.PERMIT;

    }
}
