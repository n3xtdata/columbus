/*
 * Copyright 2018 https://github.com/n3xtdata
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.n3xtdata.columbus.scheduler;


import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import com.n3xtdata.columbus.core.Check;
import com.n3xtdata.columbus.core.Schedule;
import java.util.List;
import java.util.Set;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SchedulingServiceImpl implements SchedulingService {

  private final Scheduler scheduler;

  @Autowired
  public SchedulingServiceImpl(Scheduler scheduler) {

    this.scheduler = scheduler;
  }

  @Override
  public void scheduleChecks(Set<Check> checks) {

    for (Check check : checks) {
      try {
        this.scheduleCheck(check);
      } catch (SchedulerException e) {
        e.printStackTrace();
      }
    }


  }


  private void scheduleCheck(Check check) throws SchedulerException {

    JobDetail job = this.addJob(check);

    this.addTriggers(job, check.getSchedules());


  }


  private JobDetail addJob(Check check) {

    JobDetail job;

    JobKey jobKey = new JobKey(check.getLabel(), "COLUMBUS");

    job = JobBuilder.newJob().ofType(ColumbusJob.class).withIdentity(jobKey).withDescription(check.getDescription())
        .storeDurably(true).requestRecovery(true).build();

    try {
      this.scheduler.addJob(job, false);
    } catch (SchedulerException e) {
      e.printStackTrace();
    }

    return job;

  }


  private void addTriggers(JobDetail job, List<Schedule> schedules) throws SchedulerException {

    Integer i = 0;

    for (Schedule schedule : schedules) {
      if (schedule.getType().equals("CRON")) {

        CronTrigger trigger = newTrigger().withIdentity(job.getKey().getName() + "-trigger-" + i, "COLUMBUS")
            .forJob(job)
            .withSchedule(cronSchedule(schedule.getValue())).build();

        this.scheduler.scheduleJob(trigger);


      } else if (schedule.getType().equals("SIMPLE")) {

        SimpleTrigger trigger = newTrigger().withIdentity(job.getKey().getName() + "-trigger-" + i, "COLUMBUS")
            .withSchedule(simpleSchedule().withIntervalInSeconds(Integer.parseInt(schedule.getValue())).repeatForever())
            .forJob(job)
            .build();

        this.scheduler.scheduleJob(trigger);


      }

      i = i + 1;
    }

  }


}
