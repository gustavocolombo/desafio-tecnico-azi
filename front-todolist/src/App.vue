<template>
  <v-app theme="light">
    <Header />
    <v-main>
      <v-container
        class="d-flex flex-column justify-center align-items-center mt-5"
      >
        <div class="d-flex align-items-center">
          <h1 class="text-center mr-auto">Minhas Tarefas</h1>
          <v-btn color="blue">
            <span>Criar tarefa</span>
            <v-icon>mdi-plus</v-icon>
          </v-btn>
        </div>
        <TaskList :tasks="tasks" />
      </v-container>
    </v-main>
  </v-app>
</template>

<script>
import { ref } from "vue";
import axios from "axios";
import TaskList from "./components/TaskList.vue";

export default {
  components: { TaskList },

  setup() {
    const tasks = ref([]);

    const fetchTasks = async () => {
      try {
        const response = await axios.get("http://localhost:8080/api/v1/tasks");
        tasks.value = response.data;
      } catch (error) {
        console.error("Failed at get tasks", error);
      }
    };

    fetchTasks();

    return { tasks };
  },
};
</script>
