package ru.forumcalendar.forumcalendar.controller.resources;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("editor/team_event")
@PreAuthorize("hasRole('ROLE_SUPERUSER')")
public class TeamEventResourceController {
}
