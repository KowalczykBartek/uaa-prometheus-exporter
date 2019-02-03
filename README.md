[![](https://img.shields.io/badge/unicorn-approved-ff69b4.svg)](https://www.youtube.com/watch?v=9auOCbH5Ns4)
![][license img]

## what it is ?
Result of experiments with UAA's mbeans, simple exporter of UAA's metrics to prometheus format. It include also parsed output from cloudfoundry.identity.ServerRequests.Globals (json data).
Code quality is debatable, but it works (｡◕‿‿◕｡)

## building
Just generate shadow jar
```
./gradlew clean shadowJar
java -jar -Xms32m -Xmx32m build/libs/uaa-prometheus-exporter-server-1.0-SNAPSHOT.jar
```

## UAA requirements
UAA has to accept remote JMX connection, if you are using gradle to run UAA, please add following system env to your build
```
property 'com.sun.management.jmxremote.authenticate', System.getProperty('com.sun.management.jmxremote.authenticate', 'false')
property 'com.sun.management.jmxremote.port', System.getProperty('com.sun.management.jmxremote.port', '9876')
property 'com.sun.management.jmxremote.ssl', System.getProperty('com.sun.management.jmxremote.ssl', 'ssl')
```

## output
Example output (not all metrics included):
```
# HELP cloudfoundry.identity UaaAudit
# TYPE cloudfoundry.identity summary
cloudfoundy_uaa_uaa_audit_client_authentication_count 1632.0
cloudfoundy_uaa_uaa_audit_client_authentication_failure_count 4.0
cloudfoundy_uaa_uaa_audit_principal_authentication_failure_count 0.0
cloudfoundy_uaa_uaa_audit_principal_not_found_count 0.0
cloudfoundy_uaa_uaa_audit_user_authentication_failure_count 0.0
cloudfoundy_uaa_uaa_audit_user_authentication_count 0.0
cloudfoundy_uaa_uaa_audit_user_not_found_count 0.0
cloudfoundy_uaa_uaa_audit_user_password_changes 0.0
cloudfoundy_uaa_uaa_audit_user_password_failures 0.0
# HELP generic GenericParser
# TYPE generic summary
cloudfoundy_uaa_generic_last_gc_info_p_s_scavenge_gc_thread_count 10.0
cloudfoundy_uaa_generic_last_gc_info_p_s_scavengeduration 4.0
cloudfoundy_uaa_generic_last_gc_info_p_s_scavengeend_time 73837.0
cloudfoundy_uaa_generic_last_gc_info_p_s_scavengeid 115.0
cloudfoundy_uaa_generic_last_gc_info_p_s_scavengestart_time 73833.0
cloudfoundy_uaa_generic_collection_count 115.0
cloudfoundy_uaa_generic_collection_time 710.0
# HELP cloudfoundry.identity ClientEndpoint
# TYPE cloudfoundry.identity summary
cloudfoundy_uaa_client_endpoint_client_deletes 0.0
cloudfoundy_uaa_client_endpoint_client_secret_changes 0.0
cloudfoundy_uaa_client_endpoint_client_updates_metric 0.0
cloudfoundy_uaa_client_endpoint_total_clients 23.0
# HELP spring.application DataSource
# TYPE spring.application summary
cloudfoundy_uaa_spring_datasource_max_active 100.0
cloudfoundy_uaa_spring_datasource_max_idle 10.0
cloudfoundy_uaa_spring_datasource_num_active 0.0
cloudfoundy_uaa_spring_datasource_num_idle 1.0
# HELP generic GenericParser
# TYPE generic summary
cloudfoundy_uaa_generic_last_gc_info_p_s_mark_sweep_gc_thread_count 10.0
cloudfoundy_uaa_generic_last_gc_info_p_s_mark_sweepduration 232.0
cloudfoundy_uaa_generic_last_gc_info_p_s_mark_sweepend_time 73101.0
cloudfoundy_uaa_generic_last_gc_info_p_s_mark_sweepid 5.0
cloudfoundy_uaa_generic_last_gc_info_p_s_mark_sweepstart_time 72869.0
cloudfoundy_uaa_generic_collection_count 5.0
cloudfoundy_uaa_generic_collection_time 600.0
# HELP cloudfoundry.identity UserEndpoint
# TYPE cloudfoundry.identity summary
cloudfoundy_uaa_user_endpoint_total_users 2.0
cloudfoundy_uaa_user_endpoint_user_deletes 0.0
cloudfoundy_uaa_user_endpoint_user_updates 0.0
# HELP generic GenericParser
# TYPE generic summary
cloudfoundy_uaa_generic_uptime 561579.0
cloudfoundy_uaa_generic_start_time 1.549204373101E12
# HELP cloudfoundry.identity ServerRequests
# TYPE cloudfoundry.identity summary
cloudfoundy_uaa_server_requests_count 1589.0
cloudfoundy_uaa_server_requests_average_time 1173.8514789175651
cloudfoundy_uaa_server_requests_database_query_count 14301.0
cloudfoundy_uaa_server_requests_average_database_query_time 40.032375358366494
# HELP generic GenericParser
# TYPE generic summary
cloudfoundy_uaa_generic_open_file_descriptor_count 83.0
cloudfoundy_uaa_generic_max_file_descriptor_count 10240.0
cloudfoundy_uaa_generic_committed_virtual_memory_size 6.724206592E9
cloudfoundy_uaa_generic_total_swap_space_size 1.073741824E10
cloudfoundy_uaa_generic_free_swap_space_size 1.172832256E9
cloudfoundy_uaa_generic_process_cpu_time 1.79169653E11
cloudfoundy_uaa_generic_free_physical_memory_size 4.7558656E8
cloudfoundy_uaa_generic_total_physical_memory_size 3.4359738368E10
cloudfoundy_uaa_generic_system_cpu_load 0.15371742768348076
cloudfoundy_uaa_generic_process_cpu_load 0.0011363788528482687
cloudfoundy_uaa_generic_available_processors 12.0
cloudfoundy_uaa_generic_system_load_average 5.34521484375
```

[license img]:https://img.shields.io/badge/License-Apache%202-blue.svg

